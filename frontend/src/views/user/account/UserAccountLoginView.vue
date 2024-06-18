<template>
    <div class="card login" v-if="!$store.state.user.pulling_info">
        <div class="card-body">
            <div style="font-family:STXingkai;font-size:260%;text-align:center;">
                测盟汇
            </div>
                <el-radio-group v-model="labelPosition" style="margin-top:2%;" aria-label="label position">
                    <el-radio-button value="left">靠左</el-radio-button>
                    <el-radio-button value="right">靠右</el-radio-button>
                    <el-radio-button value="top">顶部</el-radio-button>
                </el-radio-group>
                <div style="margin: 20px" />
                <el-form
                    :label-position="labelPosition"
                    label-width="auto"
                    :model="formLabelAlign"
                    style="max-width: 600px"
                    @submit.prevent="login"
                >
                    <el-form-item label="账号">
                    <el-input v-model="formLabelAlign.username" type="text" placeholder="账号" />
                    </el-form-item>
                    <el-form-item label="密码">
                    <el-input v-model="formLabelAlign.password" type="password" placeholder="密码"/>
                    </el-form-item>
                    <el-form-item label="验证码">
                        <div>
                            <el-input v-model="code" />
                                <SIdentify @click="refreshCode" :identifyCode="identifyCode" />
                        </div>
                        
                    </el-form-item>
                    <div class="error-message">{{ error_message }}</div>
                    <el-checkbox label="记住密码" value="true" />
                    <button type="submit" style="margin-top:4%;" class="btn btn-primary">提交</button>
                </el-form>
                
        </div>
    </div>
</template>

<script>
import { useStore } from 'vuex'
import router from '../../../router/index'
import { FormProps } from 'element-plus'
import { reactive, ref , onMounted } from 'vue'
import SIdentify from '../../../components/SIdentify.vue';

export default {
    components: {
        SIdentify,
    },
    setup() {
        const store = useStore();
        let error_message = ref('');
        let labelPosition = ref('right');
        let formLabelAlign = reactive({
        username: '',
        password: '',
        })

        let identifyCodes = "1234567890"
        let identifyCode = ref('3212')
        
        const randomNum = (min, max) => {
            return Math.floor(Math.random() * (max - min) + min)
        }
        
        const makeCode = (o, l) => {
            for (let i = 0; i < l; i++) {
                identifyCode.value += o[
                    randomNum(0, o.length)
                ];
            }
        }
        
        const refreshCode = () => {
            identifyCode.value = "";
            makeCode(identifyCodes, 4);
        }

        const jwt_token = localStorage.getItem("jwt_token");
        if (jwt_token) {
            store.commit("updateToken", jwt_token);
            store.dispatch("getinfo", {
                success() {
                    router.push({ name: "home" });
                    store.commit("updatePullingInfo", false);
                },
                error() {
                    store.commit("updatePullingInfo", false);
                }
            })
        } else {
            store.commit("updatePullingInfo", false);
        }

        onMounted(() => {
            identifyCode.value = "";
            makeCode(identifyCodes, 4);
        })

        const login = () => {
            error_message.value = "";
            console.log(formLabelAlign.username , formLabelAlign.password);
            store.dispatch("login", {
                username: formLabelAlign.username,
                password: formLabelAlign.password,
                success() {
                    store.dispatch("getinfo", {
                        success() {
                            router.push({ name: 'home' });
                        }
                    })
                },
                error() {
                    error_message.value = "用户名或密码错误";
                }
            })
        }

        return {
            error_message,
            login,
            labelPosition,
            formLabelAlign,
            FormProps,
            refreshCode,
            identifyCode,
            identifyCodes
        }
    }
}
</script>

<style scoped>
button {
    width: 100%;
}
div.error-message {
    color: red;
}

.login{
    width: 30%;
    opacity: 0.9;
    position: absolute;
    margin-left: 35%;
    margin-top: 6%;
}

</style>