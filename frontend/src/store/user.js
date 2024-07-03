import $ from 'jquery'

export default {
    state: {
        id: "",
        username: "",
        photo: "",
        token: "",
        is_login: false,
        pulling_info: true,  // 是否正在从云端拉取信息
        role: "",
        companyname: "",
        userrealname: "",
    },
    getters: {
    },
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
            state.role = user.role;
            state.companyname = user.companyname;
            state.userrealname = user.userrealname;
        },
        updateToken(state, token) {
            state.token = token;
        },
        logout(state) {
            state.id = "";
            state.username = "";
            state.photo = "";
            state.token = "";
            state.is_login = false;
            state.role = "";
            state.companyname = "";
            state.userrealname = "";
        },
        updatePullingInfo(state, pulling_info) {
            state.pulling_info = pulling_info;
        }
    },
    actions: {
        login(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/company/account/token/",
                type: "post",
                data: {
                    companyname: data.companyname,
                    password: data.password,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        localStorage.setItem("jwt_token", resp.token);
                        localStorage.setItem("userType", "company");
                        context.commit("updateToken", resp.token);
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
            });
        },
        user_login(context, data) {
            $.ajax({
                url: "http://127.0.0.1:3000/user/account/token/",
                type: "post",
                data: {
                    username: data.username,
                    password: data.password,
                },
                success(resp) {
                    if (resp.error_message === "success") {
                        if ((resp.role === "超级管理员" || resp.role === "租户管理员") && resp.status === "1") {
                            localStorage.setItem("jwt_token", resp.token);
                            localStorage.setItem("userType", "user");
                            context.commit("updateToken", resp.token);
                            data.success(resp);
                        } else {
                            data.error(resp);
                        }
                    } else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
            });
        },
        getinfo(context, data) {

            let userType = localStorage.getItem("userType");

            if (userType === "company") {
                $.ajax({
                    url: "http://127.0.0.1:3000/company/account/info/",
                    type: "get",
                    headers: {
                        Authorization: "Bearer " + context.state.token,
                    },
                    success(resp) {
                        if (resp.error_message === "success") {
                            context.commit("updateUser", {
                                ...resp,
                                is_login: true,
                            });
                            data.success(resp);
                        } else {
                            data.error(resp);
                        }
                    },
                    error(resp) {
                        data.error(resp);
                    }
                })
            }
            else if (userType === "user") {
                $.ajax({
                    url: "http://127.0.0.1:3000/user/account/info/",
                    type: "get",
                    headers: {
                        Authorization: "Bearer " + context.state.token,
                    },
                    success(resp) {
                        if (resp.error_message === "success") {
                            context.commit("updateUser", {
                                ...resp,
                                is_login: true,
                            });
                            data.success(resp);
                        } else {
                            data.error(resp);
                        }
                    },
                    error(resp) {
                        data.error(resp);
                    }
                })
            }
        },
        logout(context) {
            localStorage.removeItem("jwt_token");
            localStorage.removeItem("userType");
            context.commit("logout");
        }
    },
    modules: {
    }
}