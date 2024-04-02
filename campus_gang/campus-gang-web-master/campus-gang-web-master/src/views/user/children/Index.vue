<template>
    <div class="content">
        
        <div class="block">
            <el-carousel :interval="4000" type="card" height="300px">
                <el-carousel-item v-for="item in images" :key="item">
                    <img :src=item class="medium" width="100%" height="100%"/>
                </el-carousel-item>
            </el-carousel>
        </div>

        <el-row>
        <el-col :span="24"><div class="grid-content bg-purple-dark">欢迎登录---校园跑腿管理系统</div></el-col>
        </el-row>
        <el-alert
                :title="user.school.name + '- - -任务平台共' + tasks.length + '个任务'"
                :closable="false"
                type="info">
        </el-alert>
        <div class="title">
            <div class="title_center">
                <h2>{{tasks.length}}</h2>
                <p>任务数量</p>
            </div>
            <div class="title_right">
                <h2>{{users.length}}</h2>
                <p>总用户</p>
            </div>
        </div>
    </div>
</template>

<script>
    import {mapMutations, mapState} from "vuex";
    import banner2 from "@/assets/img/banner2.jpg";
    import banner3 from "@/assets/img/banner3.png";
    import banner4 from "@/assets/img/banner4.webp";
    import banner5 from "@/assets/img/banner5.jpg";

    export default {
        name: "Index",
        data() {
            return {
                tasks: [],
                users: [],
                images:[
                    banner2,
                    banner3,
                    banner4,
                    banner5,
                ]
            }
        },
        methods: {
        },
        computed: {
            ...mapState('user', ['user'])
        },
        created() {
            // console.log(this.user)
            this.$get("/task", {id: this.user.id})
                .then((res) => {
                    this.tasks = res.data.task
            })

            this.$get("/user")
            .then((rs) => {
                this.users = rs.data.user
            })

        },

    }
</script>

<style scoped lang="less">

    .el-carousel__item h3 {
        color: #475669;
        font-size: 14px;
        opacity: 0.75;
        line-height: 150px;
        margin: 0;
    }

    .el-carousel__item:nth-child(2n) {
        background-color: #99a9bf;
    }

    .el-carousel__item:nth-child(2n+1) {
        background-color: #d3dce6;
    }

    .el-row {
        margin-bottom: 20px;
        &:last-child {
        margin-bottom: 0;
        }
    }
    .el-col {
        border-radius: 4px;
    }

      .bg-purple {
    background: #d3dce6;
  }

    .content {
        background: #FFf;
        margin: 0 15px;
        padding: 15px;

        .card h2 {
            font-weight: normal;
            font-size: 18px;

            span {
                font-size: 12px;
                display: inline-block;
                border: 1px solid red;
                padding: 1px 3px;
            }
        }

        .title {
            margin: 15px 0;
            width: 100%;
            display: flex;
            height: 100px;
            justify-content: space-between;

            div {
                border-radius: 5px;
                padding: 15px;
                color: #fff;
            }

            div p {
                font-size: 12px;
                margin-top: 10px;
            }

            .title_center {
                width: 50%;
                background: #d8b34d;
            }

            .title_right {
                width: 50%;
                background: #1f68bb;
            }
        }

    }

</style>