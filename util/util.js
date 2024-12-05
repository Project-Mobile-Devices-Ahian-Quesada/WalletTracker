'use strict';

const fs = require('fs');

exports.writeFile = function(data, jsonFile){
    fs.writeFile(jsonFile, data, function(err){
        if(err) return false;
        return true;
    });
};

exports.readFile = function(jsonFile){
    return fs.readFileSync(jsonFile);
};