'use strict';

var _ = require('underscore');
var util = require('../util/util');
var jsonFile = './data/users.json';

let usersData = JSON.parse(util.readFile(jsonFile));

exports.auth_user = function(username, password){
    return usersData.filter (c=> c.username == username && c.password === password && c.isActive);    
};

exports.get_users = function(){
    return usersData;    
};