
window.Parsley.addValidator('uniquedomainname', {
    validateString: function(value,domainIdentity) {
        return validateUniquationOfDomainName(value,domainIdentity);
    },
    messages: {
        en: 'Domain name has to be unique.',
    }
});


window.Parsley.addValidator('uniquedomainpath', {
    validateString: function(value,domainIdentity) {
        return validateUniquationOfDomainPath(value,domainIdentity);
    },
    messages: {
        en: 'Domain name has to be unique.',
    }
});
window.Parsley.addValidator('uniqueentitypath', {
    validateString: function(value, entityIdentity) {
     return validateUniquationOfEntityPath(value, entityIdentity);
    },
    messages: {
        en: 'Entity path has to be unique.',
    }
});

window.Parsley.addValidator('uniquedatasetname', {
    validateString: function(value) {
        return validateUniquationOfDatasetName(value);
    },
    messages: {
        en: 'Dataset name has to be unique.',
    }
});
window.Parsley.addValidator('uniquefilepath', {
    validateString: function(value) {
        return validateUniquationOfDatasetFilePath(value);
    },
    messages: {
        en: 'Dataset file name has to be unique.',
    }
});

window.Parsley.addValidator('uniquesparqlendpoint', {
    validateString: function(value) {
        return validateUniquationOfDatasetPath(value);
    },
    messages: {
        en: 'Sparql endpoint has to be unique.',
    }
});

window.Parsley.addValidator('uniquedatasetanalysisname', {
    validateString: function(value) {
        return validateUniquationOfDatasetAnalysisName(value);
    },
    messages: {
        en: 'Dataset analysis name has to be unique.',
    }
});

window.Parsley.addValidator('maxFileSize', {
    validateString: function (_value, maxSize, parsleyInstance) {
        if (!window.FormData) {
            alert('You are making all developpers in the world cringe. Upgrade your browser!');
            return true;
        }
        var files = parsleyInstance.$element[0].files;
        var suffix = files[0].name.split(".");
        return files.length != 1 || suffix[1]=="nt" || suffix[1]=="NT";
    },
    requirementType: 'integer',
    messages: {
        en: 'This file has to have extenstion .nt or .NT',
    }
});

window.Parsley.addValidator('formathdt', {
    validateString: function (_value, maxSize, parsleyInstance) {
        if (!window.FormData) {
            alert('You are making all developpers in the world cringe. Upgrade your browser!');
            return true;
        }
        var files = parsleyInstance.$element[0].files;
        var suffix = files[0].name.split(".");
        return files.length != 1 || suffix[1]=="hdt" || suffix[1]=="HDT";
    },
    requirementType: 'integer',
    messages: {
        en: 'This file has to have extenstion .hdt',
    }
})







/*
$.formUtils.addValidator({
    name : 'domainName_unique',
    validatorFunction : function(value, $el, config, language, $form) {

        return validateUniquationOfName(value);
    },
    errorMessage : 'You have to write unique domain name',
    errorMessageKey: 'badDomainName'
});

$.formUtils.addValidator({
    name : 'entityPath_unique',
    validatorFunction : function(value, $el, config, language, $form) {

        return validateUniquationOfEntityPath(value);
    },
    errorMessage : 'You have to write unique entity path',
    errorMessageKey: 'badEntityPath'
});

$.formUtils.addValidator({
    name : 'datasetName_unique',
    validatorFunction : function(value, $el, config, language, $form) {

        return validateUniquationOfDatasetName(value);
    },
    errorMessage : 'You have to write unique dataset name',
    errorMessageKey: 'badEntityPath'
});

$.formUtils.addValidator({
    name : 'datasetAnalysisName_unique',
    validatorFunction : function(value, $el, config, language, $form) {

        return validateUniquationOfDatasetAnalysisName(value);
    },
    errorMessage : 'You have to write unique dataset analysis name',
    errorMessageKey: 'badEntityPath'
});

$.formUtils.addValidator({
    name : 'datasetPath_unique',
    validatorFunction : function(value, $el, config, language, $form) {

        return validateUniquationOfDatasetPath(value);
    },
    errorMessage : 'You have to write unique dataset analysis name',
    errorMessageKey: 'badEntityPath'
});
*/

