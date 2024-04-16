<template>
    <div class="content">

        <el-table :data="roles" :resizable="false" style="width: 100%  ">
            <el-table-column prop="name" label="角色名称" min-width="100">
            </el-table-column>
            <el-table-column prop="key" label="角色标识" min-width="120">
            </el-table-column>
            <el-table-column prop="order" label="角色排序" min-width="80">
            </el-table-column>

            <el-table-column label="禁用角色" min-width="90">
                <template slot-scope="scope">
                    <el-switch :value="scope.row.state == 1" active-color="#13ce66" inactive-color="#ff4949"
                        @change="edit(scope.row)">
                    </el-switch>
                </template>
            </el-table-column>

            <el-table-column label="操作" min-width="150">
                <template slot-scope="scope">
                    <el-button class="hvr-push" icon="el-icon-edit" size="mini"
                        @click="handleUpdate(scope.$index, scope.row)">编辑
                    </el-button>

                    <el-button class="hvr-push" icon="el-icon-delete" size="mini" type="danger"
                        @click="handleDelete(scope.$index, scope.row)">删除
                    </el-button>

                    <el-button class="hvr-push" icon="el-icon-setting" size="mini"
                        @click="openTransferDialog(scope.$index, scope.row)">用户分配
                    </el-button>

                    <el-dialog title="用户列表" :visible.sync="dialogVisible" width="50%">
                        <el-transfer v-model="selectedData" :titles="['所有用户列表', '选中用户列表']" :data="transferData" @change = "selectUsers">
                        </el-transfer>
                        <span slot="footer" class="dialog-footer">
                            <el-button @click=resetTransferData()>取消</el-button>
                            <el-button @click=handleConfirm()>确定</el-button>
                        </span>
                    </el-dialog>

                </template>
            </el-table-column>

            <!-- ------------新加入---------- -->


            <!-- 添加或修改学生管理对话框 -->
            <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
                <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                    <el-form-item label="排序" prop="order">
                        <el-input placeholder="请输入排序" v-model="form.order" />
                    </el-form-item>
                    <el-form-item label="角色名称" prop="name">
                        <el-input v-model="form.name" placeholder="请输入学校" />
                    </el-form-item>
                    <el-form-item label="角色标识" prop="key">
                        <el-input v-model="form.key" placeholder="请输入角色标识" />
                    </el-form-item>
                </el-form>
                <div slot="footer" class="dialog-footer">
                    <el-button type="primary" @click="submit()">确 定</el-button>
                    <el-button @click="cancel">取 消</el-button>
                </div>
            </el-dialog>

        </el-table>

    </div>
</template>

<script>
import { MessageBox, Transfer } from 'element-ui';

export default {
    name: "Role",

    data() {
        return {
            roles: [],
            input: '',
            select: '1',
            form: {},
            dialogVisible: false,
            // Transfer 组件的已选项
            selectedData: [],
            // Transfer 组件的数据源
            transferData: [],
            originalTransferData: [],
            pojo: {},
        }
    },

    methods: {

        selectUsers(data){
            this.selectedData =  data
        },
        //点击分配用户开启分配用户弹窗
        openTransferDialog(a, b) {
            this.dialogVisible = true;
            //记录数据
            this.pojo = b;
            this.$get("/role/users/" + b.key).then((res) => {
                this.transferData = res.data.users;
                return this.transferData = this.transferData.map(entity => ({
                    key: entity.id,
                    label: entity.username
                }))
            });
        },

        //点击确认保存操作选中用户,进行权限分配做操作
        handleConfirm() {
            this.dialogVisible = false
            let key = this.pojo.key
            let ids = ''
            for (let i = 0; i < this.selectedData.length; i++) {
                if (i === 0) {
                    ids += this.selectedData[i] + '';
                } else {
                    ids += ',' + this.selectedData[i];
                }
            }
            this.$post("/role/" + key + "/" + ids).then((res) => {
                const { status, msg } = res.data
                if (status) {
                    this.$notifyMsg("成功", msg, "success")
                    this.newList()
                    return
                } else {
                    this.$notifyMsg("失败", msg, "error")
                }
            })
            this.selectedData = []
        },

        created() {
            // 保存原始数据副本
            this.originalTransferData = [...this.transferData];
        },

        resetTransferData() {
            this.dialogVisible = false
            // 重置 Transfer 数据为原始数据
            this.transferData = [...this.originalTransferData];
            this.selectedData = []
        },

        clickSearch() {
            let parameter = {};
            if (this.input) {
                if (this.select == 1) {
                    parameter = { "name": this.input };
                } else {
                    parameter = { "key": this.input };
                }
                this.$get("/role", parameter)
                    .then((res) => {
                        this.roles = res.data.role
                        this.input = ""
                    })
            } else {
                this.newList()
            }
        },

        del(id) {
            console.log(id);
            this.$del("/role/" + id)
                .then((res) => {
                    this.$notifyMsg("成功", res.data.msg, "success")
                    this.newList()
                })
                .catch((err) => {
                    this.$notifyMsg("失败", res.data.msg, "error")
                })
        },

        edit(row) {
            if (row.state == 0) {
                row.state = 1;
            } else {
                row.state = 0;
            }
            this.$put("/role", { id: row.id, state: row.state })
                .then((res) => {
                    this.$notifyMsg("成功", res.data.msg, "success")
                    this.newList()
                })
                .catch((err) => {
                    this.$notifyMsg("失败", res.data.msg, "error")
                })
        },

        //编辑角色
        /** 修改按钮操作 */
        handleUpdate(a, b) {
            this.reset();
            this.$get("/role/" + b.id).then((res) => {
                this.form = res.data.role;
                this.open = true;
                this.title = "修改角色";
            });
        },

        // 取消按钮
        cancel() {
            this.open = false;
            resetTransferData();
        },

        //删除角色
        handleDelete(a, b) {
            if (b.id != null) {
                this.$del("/role/" + b.id)
                    .then((res) => {
                        const { status, msg } = res.data
                        if (status) {
                            this.$notifyMsg("成功", msg, "success")
                            this.newList()
                            return
                        } else {
                            this.$notifyMsg("失败", msg, "error")
                        }
                    })
            }
        },

        newList() {
            this.$get("/role")
                .then((rs) => {
                    this.roles = rs.data.role
                    console.log(this.roles)
                })
        },
    },



    created() {
        this.newList()
    },

    computed: {
        dataList() {
            return this.roles.map(item => {
                return {
                    id: item.id,
                    name: item.name,
                    key: item.key,
                    order: item.order
                }
            })
        }
    }
}


</script>

<style scoped lang="less">
.content {
    padding: 0 1%;
}

.dialog-footer {
    text-align: center;
}
</style>