"use strict";
const common_vendor = require("../common/vendor.js");
const state = {
  id: "",
  username: "",
  photo: "",
  token: "",
  is_login: false,
  pulling_info: true
  // 是否正在从云端拉取信息
};
const getters = {};
const mutations = {
  updateUser(state2, user) {
    state2.id = user.id;
    state2.username = user.username;
    state2.photo = user.photo;
    state2.is_login = user.is_login;
  },
  updateToken(state2, token) {
    state2.token = token;
  },
  logout(state2) {
    state2.id = "";
    state2.username = "";
    state2.photo = "";
    state2.token = "";
    state2.is_login = false;
  },
  updatePullingInfo(state2, pulling_info) {
    state2.pulling_info = pulling_info;
  }
};
const actions = {
  login({ commit }, data) {
    common_vendor.wx$1.request({
      url: "http://127.0.0.1:3000/user/account/token/",
      method: "POST",
      data: `username=${data.username}&password=${data.password}`,
      header: {
        "content-type": "application/x-www-form-urlencoded"
        // 确保内容类型为表单编码
      },
      success(res) {
        const resp = res.data;
        if (resp.error_message === "success") {
          if (resp.status === "1") {
            common_vendor.wx$1.setStorageSync("jwt_token", resp.token);
            common_vendor.wx$1.setStorageSync("userType", "user");
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
  getinfo({ commit, state: state2 }, data) {
    common_vendor.wx$1.request({
      url: "http://127.0.0.1:3000/user/account/info/",
      method: "GET",
      header: {
        Authorization: `Bearer ${common_vendor.wx$1.getStorageSync("jwt_token")}`
      },
      success(res) {
        const resp = res.data;
        if (resp.error_message === "success") {
          commit("updateUser", {
            ...resp,
            is_login: true
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
    common_vendor.wx$1.removeStorageSync("jwt_token");
    commit("logout");
  }
};
const ModuleUser = {
  namespaced: true,
  state,
  getters,
  mutations,
  actions
};
exports.ModuleUser = ModuleUser;
