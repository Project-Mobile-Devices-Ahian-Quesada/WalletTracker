'use strict';

var _ = require('underscore');
let model = require('../models/AuthModel');
var httpresponse = require('../util/httpResponse')
var endpointresponse = require('../util/endpointResponse')

exports.authUser = function(req, res) {
    let result={};
    let httpCode=0;
    if (req.body.username === undefined || req.body === null || req.body.length === 0 || req.body === ''){
        httpCode=httpresponse.BAD_REQUEST;
        result = {
            data: null,
            responseCode: endpointresponse.MISSING_PARAMETERS,
            message: 'Must provide the credentials for loggin.'
        };
        return;
    }

    let user = model.auth_user(req.body.username, req.body.password);
    if (user.length === 0){
        httpCode=httpresponse.BAD_REQUEST;
        result = {
            data: null,
            responseCode: endpointresponse.INFO_NOT_FOUND,
            message: 'The user/password does not match with the right credentials or username is inactive.'
        };
    }else{
        let data = {
            user: user[0].username,
            name: user[0].name,
            lastname: user[0].lastname,
            emailname: user[0].emailname
        };
        result = {
            data: data,
            responseCode: endpointresponse.INFO_FOUND,
            message: 'Action executed sucessfully.'
        };
        httpCode=httpresponse.OK;
    }
    res.status(httpCode).send(result);
};

exports.getUsers = function(req, res){
    let data = model.get_users();
    let result = {
        data: data,
        responseCode: endpointresponse.SUCESSFUL,
        message: 'Action executed sucessfully.'
    };
    res.status(httpresponse.OK).send(result);
};