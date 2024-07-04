const state = {
    id: "",
    username: "",
    photo: "",
    token: "",
    is_login: false,
    pulling_info: true,  // 是否正在从云端拉取信息
};

const getters = {};

const mutations = {
    updateUser(state, user) {
        state.id = user.id;
        state.username = user.username;
        state.photo = user.photo;
        state.is_login = user.is_login;
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
    },
    updatePullingInfo(state, pulling_info) {
        state.pulling_info = pulling_info;
    }
};

const actions = {
    login({ commit }, data) {
        wx.request({
            url: "https://app6457.acapp.acwing.com.cn/api/user/account/token/",
            method: "POST",
            data: `username=${data.username}&password=${data.password}`,
            header: {
                'content-type': 'application/x-www-form-urlencoded' // 确保内容类型为表单编码
            },
            success(res) {
                const resp = res.data;
                if (resp.error_message === "success") {
                    if (resp.status === "1") {
                        wx.setStorageSync("jwt_token", resp.token);
                        wx.setStorageSync("userType", "user");
                        commit("updateToken", resp.token);
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                } else {
                    data.error(resp);
                }
            },
            fail(err) {
                data.error(err);
            }
        });
    },
    getinfo({ commit, state }, data) {
        wx.request({
            url: "https://app6457.acapp.acwing.com.cn/api/user/account/info/",
            method: "GET",
            header: {
                Authorization: `Bearer ${wx.getStorageSync("jwt_token")}`
            },
            success(res) {
                const resp = res.data;
                if (resp.error_message === "success") {
                    commit("updateUser", {
                        ...resp,
                        is_login: true,
                    });
                    data.success(resp);
                } else {
                    data.error(resp);
                }
            },
            fail(err) {
                data.error(err);
            }
        });
    },
    logout({ commit }) {
        wx.removeStorageSync("jwt_token");
        wx.removeStorageSync("userType");
        wx.removeStorageSync("username");
        commit("logout");
    }
};

export default {
    namespaced: true,
    state,
    getters,
    mutations,
    actions,
};
