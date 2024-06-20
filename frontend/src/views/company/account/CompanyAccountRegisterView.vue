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
                :model="form"
                :rules="rules"
                ref="ruleFormRef"
                class="demo-ruleForm"
                @submit.prevent="register"
            >
                <el-form-item label="企业名称" prop="username">
                    <el-input style="caret-color: lightblue;" v-model="form.username" placeholder="企业名称" />
                </el-form-item>
                <el-form-item label="企业联系方式" prop="telephone">
                    <el-input style="caret-color: lightblue;" v-model="form.telephone" placeholder="企业联系方式" />
                </el-form-item>
                <el-form-item label="密码" prop="password">
                    <el-input style="caret-color: lightblue;" v-model="form.password" type="password" placeholder="请输入密码" />
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmedPassword">
                    <el-input style="caret-color: lightblue;" v-model="form.confirmedPassword" type="password" placeholder="请再次输入密码" />
                </el-form-item>
                <el-form-item label="验证码" prop="code">
                    <div class="container">
                        <el-input style="caret-color: lightblue;" v-model="form.code" class="input-element" />
                        <SIdentify @click="refreshCode" :identifyCode="identifyCode" class="identify-element" />
                    </div>
                </el-form-item>
                <el-form-item label="短信验证码" prop="text">
                    <div class="container">
                        <el-input style="flex: 1; width:50%;" v-model="form.textCode" placeholder="短信验证码" />
                        <el-button style="flex: 1; width:30%; margin-left:10%;" :disabled="smsDisabled" @click="sendSMSCode">{{ smsButtonText }}</el-button>
                    </div>
                </el-form-item>

                <div class="error-message">{{ error_message }}</div>
                <el-form-item>
                    <el-button type="primary" native-type="submit">注册</el-button>
                </el-form-item>
            </el-form>
        </div>
    </div>
</template>
<script>
import { reactive, ref, onMounted } from 'vue';
import router from '../../../router/index';
import $ from 'jquery';
import SIdentify from '../../../components/SIdentify.vue';

export default {
    components: {
        SIdentify,
    },
    setup() {
        const form = reactive({
            username: '',
            telephone: '',
            password: '',
            confirmedPassword: '',
            code: '',
            textCode: ''
        });

        const error_message = ref('');
        const labelPosition = ref('right');
        const identifyCodes = "1234567890";
        const identifyCode = ref('3212');
        const smsDisabled = ref(false);
        const smsButtonText = ref('获取短信验证码');

        const randomNum = (min, max) => {
            return Math.floor(Math.random() * (max - min) + min);
        };

        const makeCode = (o, l) => {
            for (let i = 0; i < l; i++) {
                identifyCode.value += o[randomNum(0, o.length)];
            }
        };

        const refreshCode = () => {
            identifyCode.value = '';
            makeCode(identifyCodes, 4);
        };

        const startSMSTimer = () => {
            let countdown = 60;
            smsDisabled.value = true;
            smsButtonText.value = `${countdown}s后重新发送`;
            const interval = setInterval(() => {
                countdown -= 1;
                smsButtonText.value = `${countdown}s后重新发送`;
                if (countdown === 0) {
                    clearInterval(interval);
                    smsButtonText.value = '获取短信验证码';
                    smsDisabled.value = false;
                }
            }, 1000);
        };

        const sendSMSCode = () => {
            // 发送短信验证码逻辑
            startSMSTimer();
        };

        onMounted(() => {
            refreshCode();
        });

        const validateUsername = (rule, value, callback) => {
            if (!value) {
                return callback(new Error('企业名称不能为空'));
            }
            callback();
        };

        const validateTelephone = (rule, value, callback) => {
            const phoneRegex = /^1[34578]\d{9}$/;
            if (!value) {
                return callback(new Error('企业联系方式不能为空'));
            } else if (!phoneRegex.test(value)) {
                return callback(new Error('请输入有效的手机号'));
            }
            callback();
        };

        const validatePassword = (rule, value, callback) => {
            if (!value) {
                return callback(new Error('密码不能为空'));
            }
            callback();
        };

        const validateConfirmedPassword = (rule, value, callback) => {
            if (!value) {
                return callback(new Error('确认密码不能为空'));
            } else if (value !== form.password) {
                return callback(new Error('两次输入的密码不一致'));
            }
            callback();
        };

        const validateCode = (rule, value, callback) => {
            if (!value) {
                return callback(new Error('验证码不能为空'));
            } else if (value !== identifyCode.value) {
                return callback(new Error('验证码不正确'));
            }
            callback();
        };

        const validateTextCode = (rule, value, callback) => {
            if (!value) {
                return callback(new Error('短信验证码不能为空'));
            }
            callback();
        };

        const rules = reactive({
            username: [{ validator: validateUsername, trigger: 'blur' }],
            telephone: [{ validator: validateTelephone, trigger: 'blur' }],
            password: [{ validator: validatePassword, trigger: 'blur' }],
            confirmedPassword: [{ validator: validateConfirmedPassword, trigger: 'blur' }],
            code: [{ validator: validateCode, trigger: 'blur' }],
            textCode: [{ validator: validateTextCode, trigger: 'blur' }]
        });

        const ruleFormRef = ref(null);

        const register = () => {
            error_message.value = '';
            const formEl = ruleFormRef.value;
            if (!formEl) return;
            formEl.validate((valid) => {
                if (valid) {
                    $.ajax({
                        url: "http://127.0.0.1:3000/company/account/register/",
                        type: "post",
                        data: {
                            companyname: form.username,
                            telephone: form.telephone,
                            password: form.password,
                            confirmedPassword: form.confirmedPassword,
                        },
                        success(resp) {
                            if (resp.error_message === "success") {
                                router.push({ name: "company_account_login" });
                            } else {
                                error_message.value = resp.error_message;
                            }
                        },
                    });
                } else {
                    error_message.value = '请正确填写表单';
                }
            });
        };

        return {
            form,
            error_message,
            register,
            rules,
            ruleFormRef,
            labelPosition,
            refreshCode,
            identifyCode,
            smsDisabled,
            smsButtonText,
            sendSMSCode
        };
    }
};
</script>
<style scoped>
button {
    width: 100%;
}

div.error-message {
    color: red;
}

.login {
    width: 30%;
    opacity: 0.9;
    position: absolute;
    margin-left: 35%;
    margin-top: 6%;
    caret-color: transparent;
}

.container {
    display: flex;
    width: 100%;
}

.input-element, .identify-element {
    flex: 1;
    max-width: 50%;
    box-sizing: border-box;
}

.input-element {
    height: 35px;
}

.identify-element {
    margin-left: 10%;
}

.el-button--primary {
    margin-left: 10px; 
}
</style>
