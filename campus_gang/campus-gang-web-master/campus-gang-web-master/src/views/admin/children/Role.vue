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

                    <!-- <el-button
                            icon="el-icon-delete"
                            size="mini"
                            type="danger"
                            @click="handleDelete(scope.$index, scope.row)">删除
                    </el-button> -->

                    <el-button class="hvr-push" icon="el-icon-setting" size="mini" @click="openTransferDialog">用户分配
                    </el-button>
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

        <el-transfer filterable :filter-method="filterMethod" filter-placeholder="请输入用户名称" v-model="value"
            :data="roles">
        </el-transfer>

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
            // Transfer 组件的数据源
            transferData: [],
            // Transfer 组件的已选项
            selectedData: []
        }
    },

    methods: {

        //用户分配弹窗
        openTransferDialog() {
            // 弹出 MessageBox 弹框
            MessageBox({
                title: '选择用户',
                message: createElement('div', {}, [
                    createElement(Transfer, {
                        props: {
                            // 传入 Transfer 组件的属性
                            data: this.transferData,
                            // 其他属性
                        },
                        // 监听 Transfer 组件的事件
                        on: {
                            // 监听事件
                        }
                    })
                ]),
                showCancelButton: true,
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                beforeClose: (action, instance, done) => {
                    // 点击确定时的回调
                    if (action === 'confirm') {
                        // 处理选中的数据
                        console.log('选中的数据:', this.selectedData);
                    }
                    done();
                }
            });
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
        },

        edit(row) {
            // console.log(row.state)
            if (row.state == 0) {
                row.state = 1;
            } else {
                row.state = 0;
            }
            this.$put("/role", { id: row.id, state: row.state })
                .then((res) => {
                    // this.$msg(res.data.msg, "success")
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
            // console.log(b);
            this.$get("/role/" + b.id).then((res) => {
                this.form = res.data.role;
                this.open = true;
                this.title = "修改角色";
            });
        },

        // 取消按钮
        cancel() {
            this.open = false;
        },

        //删除角色
        handleDelete(a, b) {
            if (b.id != null) {
                this.$del("/role/" + b.id)
                    .then((res) => {
                        this.$notifyMsg("成功", res.data.msg, "success")
                        this.newList()
                    })
            }
        },

        //分配权限



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
</style>