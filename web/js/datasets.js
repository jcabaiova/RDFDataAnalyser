/**
 * Created by Janka on 10/18/2016.
 * This script is used with HTML page datasets.html
 */
var rootUrl = pathToApplicationOnServer + 'rest/restMethod';
var datasetList;
var analysisList;
var datasetAnalysisList;
var domainList;
var entityList;
var entityGroupList;
var actualDatasetId;
var da1Domains;
var da2Domains;
var da1Entities;
var da2Entities;
var actualDataForDatasetChart;

$(document).ready(function () {
    findAllDomains();
    findAllEntities();
    findAllEntityGroups();
    findAllDatasetsAnalysis();
    checkLocalStorage();
    loadForms();
    $('#compareDetail').hide();
    loadDatasetsToSideBar();
    removeTmpFiles();
});

// --------------------------------------------- Validation functions -----------------------------------------

function validateUniquationOfDatasetName(inputName) {
    var result = 0;
    var actualName = document.getElementById("datasetNameEdit").getAttribute("value");
    $.each(datasetList, function (index, dataset) {
        if (dataset.name === inputName && actualName != inputName) {
            result = 1;
        }
    });
    return result == 0;
}

function validateUniquationOfDatasetFilePath(inputName) {
    var result = 0;
    var splitedInputName = String(inputName).split("\\");
    var compareName = splitedInputName[splitedInputName.length - 1];
    $.each(datasetList, function (index, dataset) {
        if (dataset.hdt) {
            if (dataset.fileName === compareName) {
                result = 1;
            }
        }

    });
    return result == 0;

}

function validateUniquationOfDatasetAnalysisName(inputName) {
    var result = 0;
    var actualName = document.getElementById("datasetAnalysisNameEdit").getAttribute("value");
    $.each(datasetAnalysisList, function (index, datasetAnalysis) {
        if (datasetAnalysis.name === inputName && actualName != inputName) {
            result = 1;
        }
    });
    return result == 0;

}

function validateUniquationOfDatasetPath(inputName) {
    var result = 0;
    $.each(datasetList, function (index, dataset) {
        if (dataset.fileName === inputName) {
            result = 1;
        }
    });
    return result == 0;

}

//--------------------------------------------helpful methods----------------------------------

function getDataset(id) {
    var datasetToReturn = null;
    $.each(datasetList, function (index, dataset) {
        if (dataset.id == id) {
            datasetToReturn = dataset;
            return false;
        }
    });
    return datasetToReturn;
}

function getDatasetAnalysis(id) {
    var datasetToReturn = null;
    $.each(analysisList, function (index, analysis) {
        if (analysis.id == id) {
            datasetToReturn = analysis;
            return false;
        }
    });
    return datasetToReturn;
}

function getDatasetName(id) {
    var datasetName = "";
    $.each(datasetList, function (index, dataset) {
        if (dataset.id == id) {
            datasetName = dataset.name;
        }
    });
    return datasetName;
}

function getDatasetDescription(id) {
    var datasetDescription = "";
    $.each(datasetList, function (index, dataset) {
        if (dataset.id == id) {
            datasetDescription = dataset.description;
        }
    });
    return datasetDescription;
}

function getDomainPathFromDomainName(name) {
    var domainPath = "";
    $.each(domainList, function (index, domain) {
        if (domain.name == name) {
            domainPath = domain.path;
        }
    });
    return domainPath;
}

function getDatasetOntologyPredicate(id) {
    var datasetOntologyPredicate = "";
    $.each(datasetList, function (index, dataset) {
        if (dataset.id == id) {
            datasetOntologyPredicate = dataset.ontologyPredicate;
        }
    });
    return datasetOntologyPredicate;
}

function getDatasetAnalysisName(id) {
    var datasetAnalysisName = "";
    $.each(datasetAnalysisList, function (index, datasetAnalysis) {
        if (datasetAnalysis.id == id) {
            datasetAnalysisName = datasetAnalysis.name;
        }
    });
    return datasetAnalysisName;
}

function getDatasetAnalysisDescription(id) {
    var datasetAnalysisDescription = "";
    $.each(datasetAnalysisList, function (index, datasetAnalysis) {
        if (datasetAnalysis.id == id) {
            datasetAnalysisDescription = datasetAnalysis.description;
        }
    });
    return datasetAnalysisDescription;
}

function getEntity(id) {
    var entityPath = "";
    $.each(entityList, function (index, entity) {
        if (entity.id == id) {
            entityPath = entity.path;

        }
    });
    return entityPath;
}

function getDatasetAnalysisAccordingDataset(id) {
    var listDA = new Array();
    $.each(datasetAnalysisList, function (index, da) {
        if (da.dataset.id == id) {
            listDA.push(da);
        }
    });
    return listDA;
}

function getDomainCountInDA(domainID, data) {
    var domainToReturn = 0;
    $.each(data, function (index, domainInDataset) {
        if (domainInDataset.domain.id == domainID) {
            domainToReturn = domainInDataset.countDomain;

        }
    });
    return domainToReturn;

}

function getEntityCountInDA(entityID, data) {
    var entityToReturn = 0;
    $.each(data, function (index, entityInDataset) {
        if (entityInDataset.entity.id == entityID) {
            entityToReturn = entityInDataset.countEntities;

        }
    });
    return entityToReturn;

}

function getSumCountInDa(entityList, data) {
    var sum = 0;
    $.each(data, function (index, entityInDataset) {
        $.each(entityList, function (index2, entityInSameGroup) {
            if (entityInDataset.entity.id == entityInSameGroup.id) {
                sum += entityInDataset.countEntities;
            }
        })
    });
    return sum;
}

function getEntitiesInDomain(domainId) {
    var listToreturn = new Array();
    $.each(entityList, function (index, entity) {

        if (entity.domain.id == domainId) {

            listToreturn.push(entity);

        }
    });
    return listToreturn;

}

function getDomainName(id) {
    var domainName = "";
    $.each(domainList, function (index, domain) {
        if (domain.id == id) {
            domainName = domain.name;
        }
    });
    return domainName;
}

function getEntityPath(id) {
    var entityPath = "";
    $.each(entityList, function (index, entity) {
        if (entity.id == id) {
            entityPath = entity.path;
        }
    });
    return entityPath;
}

//-------------------------------------------resize of window --------------------------------------------

$(window).resize(function () {

    for (var i = 0; i < $('.in').length; i++) {
        var analysisId = $('.in')[i].getAttribute("analysis-id");
        if ($('#chart' + analysisId).length > 0) {
            $('#chart' + analysisId).empty();
            $('#sequence' + analysisId).empty();
            if (actualDataForDatasetChart != null) {
                createVisualization(analysisId, $('.page-content').width(), $('.page-content').width() * 0.7);
            } else {
                findDataToChart(analysisId);
            }

        }
    }

    if ($(window).width() < 992) {
        $('.btnGroupDataset').addClass('btn-group-xs').removeClass('btn-group-sm')
    } else {
        $('.btnGroupDataset').addClass('btn-group-sm').removeClass('btn-group-xs')
    }

    //for change type of buttons
    if ($(window).width() < 500) {
        $(".datasetTitleRowBigger").hide();
        $(".datAnalysisTitleBigger").hide();
        $(".datasetTitleRowSmaller").show();
        $(".datAnalysisTitleSmaller").show();

    } else {
        $(".datasetTitleRowSmaller").hide();
        $(".datAnalysisTitleSmaller").hide();
        $(".datasetTitleRowBigger").show();
        $(".datAnalysisTitleBigger").show();
    }

});

//---------------------------action on first click  --------------------------------------

$('body').on('click', 'a.datasetLink', function () {

    var datasetId = $(this).attr('data-identity');

    if ($('#datasetDetail' + datasetId).length <= 0) {
        loadDatasetDetail(datasetId);
    } else {

    }
});

$('body').on('click', 'a.collapsed', function () {
    var datasetAnalysisId = $(this).attr('data-identity');

    if ($('#collapse-analysis' + datasetAnalysisId).length <= 0) {
        loadDatasetAnalysisDeatil(datasetAnalysisId);
    }
});


//------------------------------------------------------------------------action on change/keyup-----------------------------


$('body').on('change', 'input#nameDataset', function () {
    $('#formStep1').parsley().validate();
});

$('body').on('change keyup', 'input#datasetNameEdit', function () {
    $('#formEditDataset').parsley().validate();
});

$('body').on('change keyup', 'input#addedDatasetAnalysisName', function () {
    $('#formAddDatasetAnalysis').parsley().validate();
});

$('body').on('change keyup', 'input#datasetAnalysisNameEdit', function () {
    $('#formEditDatasetAnalysis').parsley().validate();
});

$('body').on('change', 'input#sparqlEndpoint', function () {

    if ($('#sparqlEndpoint').val() != "") {
        $('#inputFile').removeAttr("required");
        $('#inputFile').prop('disabled', true);
    } else {
        $('#inputFile').prop('disabled', false);
        document.getElementById('inputFile').setAttribute('required', "");
        $('#inputFile').attr('required');
        $('#formStep1').parsley().validate();
    }
    $('#formStep1').parsley().validate();
});

$('body').on('keyup change', 'input#inputFile', function () {

    if ($('#inputFile').val() != "") {

        $('#sparqlEndpoint').removeAttr("required");
        $('#sparqlEndpoint').prop('disabled', true);
    } else {
        $('#sparqlEndpoint').prop('disabled', false);
        document.getElementById('sparqlEndpoint').setAttribute('required', "");
    }
    $('#formStep1').parsley().validate();
});

$('body').on('change', 'select#dataset1select', function () {

    var datasetId = $('#dataset1select').val();
    var da = getDatasetAnalysisAccordingDataset(datasetId);
    fillDatasetsInCombobox(da, "datasetAnalysis1select");
    document.getElementById('datasetAnalysis1select').selectedIndex = -1;
});

$('body').on('change', 'select#dataset2select', function () {

    var datasetId = $('#dataset2select').val();
    var da = getDatasetAnalysisAccordingDataset(datasetId);
    fillDatasetsInCombobox(da, "datasetAnalysis2select");
    document.getElementById('datasetAnalysis2select').selectedIndex = -1;
});

$('body').on('change', 'select#datasetAnalysis1select', function () {

    if ($('#datasetAnalysis2select').val() != null) {
        loadCompareDatasets($('#datasetAnalysis1select').val(), $('#datasetAnalysis2select').val())
    }
});

$('body').on('change', 'select#datasetAnalysis2select', function () {
    if ($('#datasetAnalysis1select').val() != null) {
        loadCompareDatasets($('#datasetAnalysis1select').val(), $('#datasetAnalysis2select').val())
    }
});


// -------------------------------- action on click -----------------------------

$('#datasetAnalysisTabs a').click(function (e) {
    e.preventDefault();
    $(this).tab('show');
    var id = $(this).context.id;
    if (id == "compareTab") {
        $('#datasetContainer').hide();
        $('#compareDetail').show();

        if ($('select#dataset1').val() == null && $('select#dataset2').val() == null) {
            fillDatasetsInCombobox(datasetList, "dataset1select");
            fillDatasetsInCombobox(datasetList, "dataset2select");
            document.getElementById('dataset1select').selectedIndex = -1;
            document.getElementById('dataset2select').selectedIndex = -1;
        }
    } else {
        $('#datasetContainer').show();
        $('#compareDetail').hide();
    }
});

$('body').on('click', 'input#checkAll', function () {

    $(".checkDomain").prop('checked', $(this).prop('checked'));
});

function openEditDataset(datasetIdentity) {
    var datasetName = getDatasetName(datasetIdentity);

    $("h4.editDatasetTitle").text("Edit dataset: \"" + datasetName + "\" !");
    document.getElementById("datasetNameEdit").setAttribute("value", datasetName);
    document.getElementById("datasetDescriptionEdit").setAttribute("value", getDatasetDescription(datasetIdentity));
    document.getElementById("datasetOntologyPredicateEdit").setAttribute("value", getDatasetOntologyPredicate(datasetIdentity));

    document.getElementById("btnEditDatasetSummit").setAttribute("dataset-identity", datasetIdentity);

    $("#editDataset").modal('show');
}

function openDeleteDataset(datasetIdentity) {
    $("h4.deleteDatasetTitle").text("Delete dataset: \"" + getDatasetName(datasetIdentity) + "\" !");
    document.getElementById("btnDeleteDatasetSummit").setAttribute("dataset-identity", datasetIdentity);
    $("#deleteDataset").modal('show');
}

function openAddDatasetAnalysis(datasetIdentity) {
    document.getElementById("btnAddAnalysisSummit").setAttribute("dataset-identity", datasetIdentity);
    loadDomainsSelectToForm();
}

function openEditDatAnalysis(datasetIdenity) {

    var datasetAnalysisName = getDatasetAnalysisName(datasetIdenity);

    $("h4.editDatasetAnalysisTitle").text("Edit dataset analysis: \"" + datasetAnalysisName + "\" !");
    document.getElementById("datasetAnalysisNameEdit").setAttribute("value", datasetAnalysisName);
    document.getElementById("datasetAnalysisDescriptionEdit").setAttribute("value", getDatasetAnalysisDescription(datasetIdenity));
    document.getElementById("btnEditDatasetAnalysisSummit").setAttribute("dataset-identity", datasetIdenity);

    $("#editDatasetAnalysis").modal('show');
}

function openDeleteDatAnalysis(datasetIdentity) {
    $("h4.deleteDatasetAnalysisTitle").text("Delete dataset analysis: \"" + getDatasetAnalysisName(datasetIdentity) + "\" !");
    document.getElementById("btnDeleteDatasetAnalysisSummit").setAttribute("dataset-identity", datasetIdentity);
    $("#deleteDatasetAnalysis").modal('show');
}

$('body').on('click', 'a.datasetLink', function () {
    actualDatasetId = $(this).attr("data-identity");
    $('.datasetDetailContainer').show();
    $('.datasetDetail').hide();
    $('#datasetDetail' + actualDatasetId).show();


});

$('body').on('click', 'tr.domainTitle', function () {
    var domainId = $(this).attr("domain-id");
    if ($(".domainSpan" + domainId).hasClass("glyphicon-minus")) {
        $(".domainSpan" + domainId).removeClass("glyphicon-minus").addClass("glyphicon-plus");
        $(".group" + domainId).hide();
    } else {
        $(".domainSpan" + domainId).removeClass("glyphicon-plus").addClass("glyphicon-minus");
        $(".group" + domainId).show();
    }

});

$('body').on('click', 'tr.entityTitle', function () {

    var entityId = $(this).attr("entity-id");
    if ($(".entitySpan" + entityId).hasClass("glyphicon-minus")) {
        $(".entitySpan" + entityId).removeClass("glyphicon-minus").addClass("glyphicon-plus");
        $(".groupPredicate" + entityId).hide();
    } else {
        $(".entitySpan" + entityId).removeClass("glyphicon-plus").addClass("glyphicon-minus");
        $(".groupPredicate" + entityId).show();
    }

});

//----------------------------------------submit form--------------------------------

//used in modal
function insertDataset() {
    var formData = datasetFormToFormData();

    //var data=datasetFormToJSON();
    $('#alertMessage').append('<div class="alert alert-info alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Info!</strong> Processing of dataset and defaul dataset analysis calculation. </div>');
    $('#addDatasetStep1').hide();
    $('#addDatasetStep2').show();

    addDataset(formData);

}

$('body').on('click', 'button.btnFinishDatasetSummit', function () {

    if (!$('#formStep1').isValid(lang, conf, false)) {

    } else {
        var formData = datasetFormToJSON();

        //var data=datasetFormToJSON();
        $('#alertMessage').append('<div class="alert alert-info alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Info!</strong> Processing of dataset and defaul dataset analysis calculation. </div>');
        $('#addDatasetStep1').hide();
        $('#addDatasetStep2').show();
        addDataset(formData);
    }


});

//used in modal
function editDatasetFunction() {

    var datasetId = $('input#btnEditDatasetSummit').attr("dataset-identity");
    var data = datasetFormEditToJSON();
    $("#editDataset").modal('hide');
    editDataset(data, datasetId);
}

$('body').on('click', 'button#btnDeleteDatasetSummit', function () {

    var datasetId = $(this).attr("dataset-identity");
    $("#deleteDataset").modal('hide');
    deleteDataset(datasetId);

});

//used in modal
function editDatasetAnalysisFunction() {
    var datasetAnalysisId = $('input#btnEditDatasetAnalysisSummit').attr("dataset-identity");
    var data = datasetAnalysisFormEditToJSON();
    $("#editDatasetAnalysis").modal('hide');
    editDatasetAnalysis(data, datasetAnalysisId);
}

$('body').on('click', 'button#btnDeleteDatasetAnalysisSummit', function () {

    var datasetAnalysisId = $(this).attr("dataset-identity");
    $("#deleteDatasetAnalysis").modal('hide');
    deleteDatasetAnalysis(datasetAnalysisId);

});

//used in modal
function addDatasetAnalysisFunction() {
    var datasetId = $('input#btnAddAnalysisSummit').attr("dataset-identity");
    var data = datasetAnalysisFormAddToJSON();
    $('#alertMessage').append('<div class="alert alert-info alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Info!</strong> Processing of dataset analysing calculation. </div>');
    $('#addDAStep1').hide();
    $('#addDAstep2').show();
    $("#idBody").css("padding-right", "0px");
    addDatasetAnalysis(datasetId, data);

}

//--------------------------------- loading data to html tags ----------------------------------

function loadDatasetsToSideBar() {
    findAllDatasets();
    $.each(datasetList, function (index, dataset) {
        $('#datasetListSideBar').append('<li class="list-group-item"><a href="#" class="datasetLink" id= "dataset' + dataset.id + '" data-identity="' + dataset.id + '">' + dataset.name + '</a></li>');
    });
}

function loadDatasetDetail(datasetId) {
    var sizeOfClass = "";
    if ($(window).width() < 992) {
        sizeOfClass = "btn-group-xs";
    } else {
        sizeOfClass = "btn-group-sm";
    }

    var dataset = getDataset(datasetId);
    $('.datasetDetailContainer').append('<div class="datasetDetail" id="datasetDetail' + dataset.id + '"> </div>');


    //create dataset title row - bigger
    $('#datasetDetail' + dataset.id).append('<div class="datasetTitleRowBigger row margin-top-20"><div class="col-md-8 col-sm-7 col-xs-7 datasetDetailTitle"><h3 class="no-margin-top">' + dataset.name + '</h3></div><div class="datasetDetailButtons col-md-4 col-sm-5 col-xs-5"><div class="btnGroupDataset btn-group ' + sizeOfClass + '" role="group" aria-label="..." > <button type="button" class="btn btn-default btnEditDataset" id="btnEditDataset' + dataset.id + '" dataset-identity="' + dataset.id + '" onclick="openEditDataset(' + dataset.id + ')"> Edit</button> <button type="button" class="btn btn-default btnDeleteDataset" id="btnDeleteDataset' + dataset.id + '" dataset-identity="' + dataset.id + '" onclick="openDeleteDataset(' + dataset.id + ')">Delete</button>' +
        '<button type="button" onclick="openAddDatasetAnalysis(' + dataset.id + ')" class="btn btn-default btnAddAnalysis" id="btnAddAnalysis' + dataset.id + '" dataset-identity="' + dataset.id + '" data-toggle="modal" data-target="#addDatasetAnalysis">Add analysis</button></div></div></div>');

    //create dataset title row - smalller
    $('#datasetDetail' + dataset.id).append('<div class="datasetTitleRowSmaller row margin-top-20"><div class="col-md-8 col-sm-8 col-xs-10 datasetDetailTitle"><h3 class="no-margin-top">' + dataset.name + '</h3></div><div class="datasetDetailButtons col-md-4 col-sm-4 col-xs-2">' +
        '<div class="btnGroupDataset btn-group" role="group" aria-label="..."><div class="btnGroupDataset btn-group ' + sizeOfClass + '" role="group">' +
        '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">' +
        '<span class="caret"></span> </button> <ul class="dropdown-menu dropdown-menu-right">' +
        '<li><a href="#" onclick="openEditDataset(' + dataset.id + ')" class="btnEditDataset" id="btnEditDataset' + dataset.id + '" dataset-identity="' + dataset.id + '">Edit</a></li>' +
        '<li><a href="#" onclick="openDeleteDataset(' + dataset.id + ')" class="btnDeleteDataset" id="btnDeleteDataset' + dataset.id + '" dataset-identity="' + dataset.id + '">Delete</a></li>' +
        '<li><a href="#" onclick="openAddDatasetAnalysis(' + dataset.id + ')" class="btnAddAnalysis" id="btnAddAnalysis' + dataset.id + '" dataset-identity="' + dataset.id + '" data-toggle="modal" ' +
        'data-target="#addDatasetAnalysis">Add dataset</a></li> </ul> </div></div></div></div>');

    if ($(window).width() < 500) {
        $(".datasetTitleRowBigger").hide();
    } else {
        $(".datasetTitleRowSmaller").hide();
    }
    //create dataset info table
    $('#datasetDetail' + dataset.id).append('<div id="table' + dataset.id + '"> <table class="table table-bordered table-responsive">' +
        '<tr> <th>Property</th><th>Value</th></tr>' +
        '<tr> <td>Name</td> <td>' + dataset.name + '</td></tr>' + '<tr> <td>Description</td> <td>' + dataset.description + '</td></tr>' + '<tr> <td>Path</td> <td>' + dataset.fileName + '</td></tr>' +
        '<tr> <td>Id</td> <td>' + dataset.id + '</td></tr>' +
        '<tr> <td>Ontology predicate</td> <td>' + dataset.ontologyPredicate + '</td></tr>' +
        '</table></div>');
    //create dataset analysis detail
    $('#datasetDetail' + dataset.id).append('<div class="row"><h4 class="datAnalysisMainTitle">Dataset Analysis</h4></div>');
    $('#datasetDetail' + dataset.id).append('<div class="row"><div class="panel-group" id="analysisPanel' + dataset.id + '" role="tablist" aria-multiselectable="true"></div></div>');
    findAllDatasetsAnalysisInDataset(dataset);

}

function loadDatasetAnalysisToPanel(analysisList, dataset) {
    $.each(analysisList, function (index, analysis) {
        //load analysis to panel
        $('#analysisPanel' + dataset.id).append('<div class="panel panel-default panelAnalysis" id="panelAnalysis' + analysis.id + '"></div>');
        $('#panelAnalysis' + analysis.id).append('<div class="panel-heading" role="tab" id="headingAnalysis' + analysis.id + '">');

        //bigger title
        $('#headingAnalysis' + analysis.id).append('<div class="row datAnalysisTitleBigger"><div class="col-md-9 col-sm-8 col-xs-7"><h4 class="panel-title">' +
            '<a role="button" class="collapsed" data-toggle="collapse" data-parent="#analysisPanel' + dataset.id + '" href="#collapse-analysis' + analysis.id + '" data-identity="' + analysis.id + '" aria-expanded="false" aria-controls="collapse-analysis' + analysis.id + '">' + analysis.name + '</a>' +
            '</h4></div> <div class="datAnalysisButtons col-md-3 col-sm-4 col-xs-5 datasetDetailButtons"> <div class="btn-group btn-group-xs" role="group" aria-label="...">' +
            '<button type="button" onclick="openEditDatAnalysis(' + analysis.id + ')" class="btn btn-default btnEditAnalysis" id="btnEditAnalysis' + analysis.id + '" dataset-identity="' + analysis.id + '"> Edit</button>' +
            '<button type="button" onclick="openDeleteDatAnalysis(' + analysis.id + ')" class="btn btn-default btnDeleteAnalysis" id="btnDeleteAnalysis' + analysis.id + '" dataset-identity="' + analysis.id + '">Delete</button>' +
            ' <div class="btn-group btn-group-xs"> <button type="button" class="btn btn-default dropdown-toggle btnExportAnalysis" id="btnExportAnalysis' + analysis.id + '" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">' +
            'Export <span class="caret"></span> </button> <ul class="dropdown-menu dropdown-menu-right">' +
            '<li><a id="btnExportAnalysisExcel" href="rest/files/analysis/xlsx/' + analysis.id + '">Export to Excel</a></li>' +
            '<li><a id="btnExportAnlaysisCSV" href="rest/files/analysis/csv/' + analysis.id + '">Export to CSV</a></li>' +
            '<li><a id="btnExportAnlaysisNTriples" href="rest/files/analysis/ntriples/' + analysis.id + '">Export to N-Triples</a></li></ul> </div></div></div></div>');

        //smaller title
        $('#headingAnalysis' + analysis.id).append('<div class="row datAnalysisTitleSmaller"><div class="col-md-9 col-sm-8 col-xs-10"><h4 class="panel-title">' +
            '<a role="button" class="collapsed" data-toggle="collapse" data-parent="#analysisPanel' + dataset.id + '" href="#collapse-analysis' + analysis.id + '" data-identity="' + analysis.id + '" aria-expanded="false" aria-controls="collapse-analysis' + analysis.id + '">' + analysis.name + '</a>' +
            '</h4></div> <div class="datAnalysisButtons col-md-3 col-sm-4 col-xs-2 datasetDetailButtons">' +
            '<div class="btn-group-xs smalletBtnGroupDataset" role="group" aria-label="..."><div class="btn-group-xs role="group">' +
            '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">' +
            '<span class="caret"></span> </button> <ul class="dropdown-menu dropdown-menu-right">' +
            '<li><a href="#" onclick="openEditDatAnalysis(' + analysis.id + ')" class="btnEditAnalysis" id="btnEditAnalysis' + analysis.id + '" dataset-identity="' + analysis.id + '">Edit</a></li>' +
            '<li><a href="#" onclick="openDeleteDatAnalysis(' + analysis.id + ')" class="btnDeleteAnalysis" id="btnDeleteAnalysis' + analysis.id + '" dataset-identity="' + analysis.id + '">Delete</a></li>' +
            '<li><a href="rest/files/analysis/xlsx/' + analysis.id + '" onclick="" class="exportAnalysis" id="btnExportAnalysisExcel' + analysis.id + '" dataset-identity="' + analysis.id + '">Export to Excel</a></li>' +
            '<li><a href="rest/files/analysis/csv/' + analysis.id + '" onclick="" class="exportAnalysis" id="btnExportAnalysisCSV' + analysis.id + '" dataset-identity="' + analysis.id + '">Export to CSV</a></li>' +
            '<li><a href="rest/files/analysis/ntriples/' + analysis.id + '" onclick="" class="exportAnalysis" id="btnExportAnlaysisNTriples' + analysis.id + '" dataset-identity="' + analysis.id + '">Export to N-Triples</a></li></ul> </div></div></div></div>');
    });

    if ($(window).width() < 500) {
        $(".datAnalysisTitleBigger").hide();
    } else {
        $(".datAnalysisTitleSmaller").hide();
    }
}

function loadDatasetAnalysisDeatil(analysisId) {
    var analysis = getDatasetAnalysis(analysisId);
    $('#panelAnalysis' + analysis.id).append('<div id="collapse-analysis' + analysis.id + '" analysis-id="' + analysis.id + '" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingAnalysis' + analysis.id + '">');
    $('#collapse-analysis' + analysis.id).append('<div class="panel-body row"> <table class="tableAnalysis table table-bordered table-hover" id="tableAnalysis' + analysis.id + '"><tr><th>Property</th><th>Value</th></tr>' +
        '<tr><td>Id</td><td>' + analysis.id + '</td></tr><tr><td>Name</td><td>' + analysis.name + '</td></tr><tr><td>Description</td><td>' + analysis.description + '</td></tr>' +
        '<tr><td>Domain count</td><td>' + analysis.domainCount + '</td></tr><tr><td>Entity count</td><td>' + analysis.entityCount + '</td></tr>' +
        '<tr><td>Short calculation</td><td>' + analysis.shortCalculationWithouPredicates + '</td></tr>' +
        '</table></div> ');

    $('#collapse-analysis' + analysis.id).append('<div class="panel-body" id="analysisPart2' + analysis.id + '"><div id="sequence' + analysis.id + '"></div><div role="tooltip" id="tooltip"></div><div id="chart' + analysis.id + '"> </div></div>');
    //createVisualization(analysis.id, $('#panelAnalysis' + analysisId).width(), 0.7 * $('#panelAnalysis' + analysisId).width());
    findDataToChart(analysis.id);
    // load content of analysis - part3
    //old part 3 - nepaci sa mi
    $('#collapse-analysis' + analysis.id).append('<div class="panel-body" id="analysisPart3' + analysis.id + '"><h4>Entities details</h4></div>');
    $('#analysisPart3' + analysis.id).append('<div class="input-group"> <input id="searchInput' + analysisId + '" analysis-id="' + analysisId + '" type="text" class="form-control searchDatasetInput" placeholder="Search for...">' +
        '<span class="input-group-btn"> <button id ="searchBtn' + analysisId + '" class="btn btn-default searchDatasetBtn" analysis-id="' + analysisId + '" type="button">Go!</button> </span> </div>');


    $('#analysisPart3' + analysis.id).append('<div class="table-responsive"><table id="tableEntityDetal' + analysis.id + '"class="table table-bordered"></table></div>');
    findDomainsInDataset(analysis.id);
}

function loadDomainsInEntityDetailTable(domainList, analysisId) {
    $.each(domainList, function (index, domain) {
        $('#tableEntityDetal' + analysisId).append('<tr class="domainTitle groupboxDomain bold domainTitleId' + domain.domain.id + '" id="domainTitle' + domain.id + '" domain-id=' + domain.id + '><td class="tableDomainTd"> <span class="glyphicon glyphicon-plus domainSpan' + domain.id + '" aria-hidden="true"></span>' + getDomainName(domain.domain.id) + '</td><td>' + domain.countDomain + '</td></tr>');
        findEntitiesInDataset(analysisId, domain.id);

    })
}

function loadEntitiesInEntityDetailTable(entityList, analysisId, domainId) {
    // var entitiesInDomain=getEntitiesInDomain(domainId);
    $.each(entityList, function (index, entity) {
        //var entity1=getEntityCountInDA(entity.id,da1Entities);
        $('#tableEntityDetal' + analysisId).append('<tr id="entityTitle' + entity.id + '" class="group' + domainId + ' entityTitle groupboxEntity" entity-id="' + entity.id + '" hidden><td class="tableEntityTd"><span class="glyphicon glyphicon-plus entitySpan' + entity.id + '" aria-hidden="true"></span>' + getEntityPath(entity.entity.id) + '</td><td>' + entity.countEntities + '</td></tr>');
        findPredicatesInEntities(entity.id, analysisId);
    })
}

function loadPredicatesToTable(predicatesList, entityId, analysisId) {
    $.each(predicatesList, function (index, predicate) {
        $('#tableEntityDetal' + analysisId).append('<tr class="predicateTitle groupPredicate' + entityId + '" hidden><td class="tablePredicateTd">' + predicate.name + '</td><td>' + predicate.countPredicates + '</td></tr>');
    });
}

function loadDomainsSelectToForm() {
    var checkboxes = document.getElementsByClassName('checkDomain');

    if (checkboxes.length <= 0) {
        if (domainList.length >= 8) {
            $('#checkboxes').append('<div class="row" id="dividedDomains"></div>');
            $('#dividedDomains').append('<div class="col-md-6 col-sm-6 col-xs-6" id="dividedDomainsCol1"></div>');
            $('#dividedDomains').append('<div class="col-md-6 col-sm-6 col-xs-6" id="dividedDomainsCol2"></div>');
            var half = Math.round(domainList.length / 2);
            $.each(domainList, function (index, domain) {
                if (index <= half) {
                    $('#dividedDomainsCol1').append('<div class="checkbox"><label class="checkbox-inline"><input id=checkboxDomain' + domain.id + ' class="checkDomain" type="checkbox" name="domainCheckboxes" value="' + domain.name + '" checked>' + domain.name + '</label></div>')

                } else {
                    $('#dividedDomainsCol2').append('<div class="checkbox"><label class="checkbox-inline"><input id=checkboxDomain' + domain.id + ' class="checkDomain" type="checkbox" name="domainCheckboxes" value="' + domain.name + '" checked>' + domain.name + '</label></div>')

                }
            });
        } else {
            $.each(domainList, function (index, domain) {
                $('#checkboxes').append('<div class="checkbox"><label class="checkbox-inline"><input id=checkboxDomain' + domain.id + ' class="checkDomain" type="checkbox" name="domainCheckboxes" value="' + domain.name + '" checked>' + domain.name + '</label></div>')
            });
        }

    }
}

function loadCompareDatasets(datasetAnalysis1, datasetAnalysis2) {

    if ($('tr.domainTitle').length > 0) {
        $('#compareModeTable tr.domainTitle').remove();
        $('#compareModeTable td').remove();
    }
    findDomainsInDatasetForCompare(datasetAnalysis1, 1);
    findDomainsInDatasetForCompare(datasetAnalysis2, 2);
    findEntitiesInDatasetForCompare(datasetAnalysis1, 1);
    findEntitiesInDatasetForCompare(datasetAnalysis2, 2);

    $.each(domainList, function (index, domain) {
        var domain1 = getDomainCountInDA(domain.id, da1Domains);
        var domain2 = getDomainCountInDA(domain.id, da2Domains);
        $('#compareModeTable').append('<tr class="domainTitle groupDomain groupboxDomain bold" domain-id=' + domain.id + '><td><span class="glyphicon glyphicon-minus domainSpan' + domain.id + '" aria-hidden="true"></span>' + domain.name + '</td><td>' + domain1 + '</td><td>' + domain2 + '</td></tr>');
        var entitiesInDomain = getEntitiesInDomain(domain.id);

        $.each(entityGroupList, function (index, entityGroup) {
            var entityListInSameGroup = new Array();
            $.each(entitiesInDomain, function (index, entity) {
                if (entity.entityGroup.id == entityGroup.id) {
                    entityListInSameGroup.push(entity);
                }
            });
            if (entityListInSameGroup != null) {
                if (entityListInSameGroup.length == 1) {
                    var entity1 = getEntityCountInDA(entityListInSameGroup[0].id, da1Entities);
                    var entity2 = getEntityCountInDA(entityListInSameGroup[0].id, da2Entities);
                    $('#compareModeTable').append('<tr class="group' + domain.id + '"><td>' + entityListInSameGroup[0].path + '</td><td>' + entity1 + '</td><td>' + entity2 + '</td></tr>');
                } else if (entityListInSameGroup.length > 1) {
                    var entity1 = getSumCountInDa(entityListInSameGroup, da1Entities);
                    var entity2 = getSumCountInDa(entityListInSameGroup, da2Entities);


                    $.each(entityListInSameGroup, function (index, entity) {
                        if (index == 0) {
                            $('#compareModeTable').append('<tr class="group' + domain.id + '"><td>' + entity.path + '</td><td rowspan="' + entityListInSameGroup.length + '">' + entity1 + '</td><td rowspan="' + entityListInSameGroup.length + '">' + entity2 + '</td></tr>');
                        } else {
                            $('#compareModeTable').append('<tr class="group' + domain.id + '"><td>' + entity.path + '</td></tr>');

                        }

                    })
                }
            }
        })

    })

}


//----------------------------- get data ------------------------------------
function findAllDatasets() {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/getDatasets",
        dataType: "json",
        async: false,
        success: function (data) {
            datasetList = data;
        }
    })
}

function findAllDatasetsAnalysis() {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/getDatasetAnalysis",
        dataType: "json",
        async: false,
        success: function (data) {
            datasetAnalysisList = data;
        }
    })
}

function findAllDatasetsAnalysisInDataset(dataset) {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/getDatasetAnalysis/" + dataset.id,
        dataType: "json",
        async: false,
        success: function (data) {
            analysisList = data;
            loadDatasetAnalysisToPanel(data, dataset);
        }
    })
}

function findPredicatesInEntities(entityId, analysisId) {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/getPredicatesInEntityInDataset/" + entityId,
        dataType: "json",
        async: false,
        success: function (data) {
            loadPredicatesToTable(data, entityId, analysisId);
        }
    })
}

function findAllDomains() {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/getDomains",
        dataType: "json",
        async: false,
        success: function (data) {
            domainList = data;
            loadDomainsSelectToForm();
        }
    })
}

function findAllEntities() {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/getEntities",
        dataType: "json",
        async: false,
        success: function (data) {
            entityList = data;
        }
    })
}

function findAllEntityGroups() {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/getEntitiesGroup",
        dataType: "json",
        async: false,
        success: function (data) {
            entityGroupList = data;
        }
    })
}

function findDomainsInDatasetForCompare(datasetAnalysisId, order) {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/getDomainsInDatasetAnalysis/" + datasetAnalysisId,
        dataType: "json",
        async: false,
        success: function (data) {
            if (order == 1) {
                da1Domains = data;
            }
            if (order == 2) {
                da2Domains = data;
            }

        },
        error: function () {
            alert("err");
        }
    })
}

function findDomainsInDataset(datasetAnalysisId) {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/getDomainsInDatasetAnalysis/" + datasetAnalysisId,
        dataType: "json",
        async: false,
        success: function (data) {
            loadDomainsInEntityDetailTable(data, datasetAnalysisId);
        },
        error: function () {
            alert("err");
        }
    })
}

function findEntitiesInDatasetForCompare(datasetAnalysisId, order) {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/getEntitiesInDatasetAnalysis/" + datasetAnalysisId,
        dataType: "json",
        async: false,
        success: function (data) {
            if (order == 1) {
                da1Entities = data;
            }
            if (order == 2) {
                da2Entities = data;
            }
        }
    })
}

function findEntitiesInDataset(datasetAnalysisId, domainId) {

    $.ajax({
        type: 'GET',
        url: rootUrl + "/getEntitiesInDatasetAnalysis/" + datasetAnalysisId + "/" + domainId,
        dataType: "json",
        async: false,
        success: function (data) {

            loadEntitiesInEntityDetailTable(data, datasetAnalysisId, domainId);
        }
    })
}

function findDataToChart(datasetAnalysisId) {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/getDataForDatasetAnalysisChart/" + datasetAnalysisId,
        dataType: "json",
        async: false,
        success: function (data) {
            actualDataForDatasetChart = data;
            createVisualization(datasetAnalysisId, $('.page-content').width(), $('.page-content').width() * 0.7);
        },
        error: function () {
            alert("error");
        }
    })
}

//-----------------------------------------edit data -----------------------

function editDataset(data, datasetId) {
    $.ajax({
        type: 'POST',
        url: rootUrl + "/updateDataset/" + datasetId,
        dataType: "json",
        contentType: 'application/json',
        data: data,
        async: false,
        success: function (data) {

            var status = "successful";
            localStorage.setItem("EditDatasetScs", status);
            localStorage.setItem("DatasetId", status);
            window.location.reload(false);

        },
        error: function () {
            $('#alertMessage').append('<div class="alert alert-danger alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Error!</strong> Editation of dataset was not sucssessfull. </div>');

        }
    })
}

function editDatasetAnalysis(data, datasetAnalysisId) {
    $.ajax({
        type: 'POST',
        url: rootUrl + "/updateDatasetAnalysis/" + datasetAnalysisId,
        dataType: "json",
        contentType: 'application/json',

        data: data,
        async: false,
        success: function (data) {
            var status = "successful";
            localStorage.setItem("EditDatasetAnalysisScs", status);
            window.location.reload(false);
        },
        error: function () {
            $('#alertMessage').append('<div class="alert alert-danger alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Error!</strong> Editation of dataset analysis was not successfull. </div>');

        }
    })
}


//------------------------------------------delete data ------------------------------

function deleteDataset(datasetId) {
    $.ajax({
        type: 'DELETE',
        url: rootUrl + "/deleteDataset/" + datasetId,
        async: false,
        success: function () {
            var status = "successful";
            localStorage.setItem("DeleteDatasetScs", status);
            $('#datasetDetail' + datasetId).empty();
            $('#dataset' + datasetId).empty();
            window.location.reload(false);
        },
        error: function (data) {
            $('#alertMessage').append('<div class="alert alert-warning alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Error!</strong>Delete of chosen dataset was not successfull.</div>');

        }
    })
}

function deleteDatasetAnalysis(datasetAnalysisId) {
    $.ajax({
        type: 'DELETE',
        url: rootUrl + "/deleteDatasetAnalysis/" + datasetAnalysisId,
        async: false,
        success: function () {
            var status = "successful";
            localStorage.setItem("DeleteDatasetAnalysisScs", status);
            $('#panelAnalysis' + datasetAnalysisId).empty();
            window.location.reload(false);


        },
        error: function (data) {
            $('#alertMessage').append('<div class="alert alert-warning alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Error!</strong>Delete of chosen dataset analysis was not successfull.</div>');

        }
    })
}

function removeTmpFiles() {
    $.ajax({
        type: 'DELETE',
        url: rootUrl + "/deleteTmpFiles",
        async: false,
        success: function () {
            
        }
    })
}

//---------------------------------------------- add data -------------------------------

function addDataset(data) {
    $.ajax({
        type: 'POST',
        url: rootUrl + "/createDataset",
        processData: false,
        contentType: false,
        data: data,
        async: true,
        success: function (data2) {
            if (data2 == null) {
                $('#alertMessage').append('<div class="alert alert-danger alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Error!</strong>Add of dataset was not successfull.</div>');
            } else {
                var status = "successful";
                localStorage.setItem("AddDatasetScs", status);
                window.location.reload(false);
            }
        },
        error: function (data2) {
            $('#alertMessage').append('<div class="alert alert-danger alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Error!</strong>Add of dataset was not successfull.</div>');


        }
    })
}

function addDatasetAnalysis(datasetId, data) {
    $.ajax({
        type: 'POST',
        url: rootUrl + "/createDatasetAnalysis/" + datasetId,
        contentType: "application/json",
        data: data,
        async: true,
        success: function (data2) {

            var status = "successful";
            localStorage.setItem("AddDatasetAnalysisScs", status);
            window.location.reload(false);

        },
        error: function (data2) {
            $('#alertMessage').append('<div class="alert alert-danger alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Error!</strong>Add of dataset analysis was not successfull.</div>');

        }
    })
}


//--------------------------------------transform to jsob object ---------------------------

function datasetFormToJSON() {
    //dodelat, ak to bude hdt file - fileName
    var file = "";
    if ($('#sparqlEndpoint').val() != "") {
        file = $('#sparqlEndpoint').val();
    }
    return JSON.stringify({
        "id": 0,
        "name": $('#nameDataset').val(),
        "description": $('#descriptionDataset').val(),
        "fileName": file
    });

}

function datasetFormToFormData() {
    var formData = new FormData();
    var fileInput = document.getElementById('inputFile');
    if (fileInput != "") {
        var file = fileInput.files[0];
        formData.append('file', file);
    } else {
    }
    formData.append('nameDataset', $('#nameDataset').val());
    formData.append('descriptionDataset', $('#descriptionDataset').val());
    formData.append('sparql', $('#sparqlEndpoint').val());
    formData.append('ontologyPredicate', $('#ontologyPredicate').val());
    formData.append('shortCalculation', $('#shortCalculation').prop('checked'));
    return formData;

}

function datasetFormEditToJSON() {
    //dodelat, ak to bude hdt file - fileName

    return JSON.stringify({
        "name": $('#datasetNameEdit').val(),
        "description": $('#datasetDescriptionEdit').val(),
        "ontologyPredicate": $('#datasetOntologyPredicateEdit').val()
    });

}

function datasetAnalysisFormEditToJSON() {
    //dodelat, ak to bude hdt file - fileName

    return JSON.stringify({
        "name": $('#datasetAnalysisNameEdit').val(),
        "description": $('#datasetAnalysisDescriptionEdit').val()
    });

}

function datasetAnalysisFormAddToJSON() {
    var checkboxes = document.getElementsByName('domainCheckboxes');
    var checboxesReturn = "";
    for (var i = 0; i < checkboxes.length; i++) {
        // And stick the checked ones onto an array...
        if (checkboxes[i].checked) {
            checboxesReturn += getDomainPathFromDomainName(checkboxes[i].value);
            checboxesReturn += ";";
        }
    }
    return JSON.stringify({
        "name": $('#addedDatasetAnalysisName').val(),
        "description": $('#addedDatasetAnalysisDescription').val(),
        "shortCalculationWithouPredicates": $('#shortCalculation').prop('checked'),
        "domainsFromInput": checboxesReturn
    });

}


function fillDatasetsInCombobox(data, comboboxId) {

    var select = document.getElementById(comboboxId);
    $('#' + comboboxId)
        .find('option')
        .remove()
        .end()
    ;
    $.each(data, function (index, dataset) {
        var option = document.createElement('option');
        option.text = dataset.name;
        option.value = dataset.id;
        select.add(option, 0);
    })


}

//----------------------------------------------------loading helpful functions ------------------------------------------------
function loadForms() {
    $('#insertDataset').load(pathToApplicationOnServer+'forms/insertDatasetForm.html');
    $('#editDataset').load(pathToApplicationOnServer+'forms/editDatasetForm.html');
    $('#deleteDataset').load(pathToApplicationOnServer+'forms/deleteDatasetForm.html');
    $('#editDatasetAnalysis').load(pathToApplicationOnServer+'forms/editDatasetAnalysisForm.html');
    $('#deleteDatasetAnalysis').load(pathToApplicationOnServer+'forms/deleteDatasetAnalysisForm.html');
    $('#addDatasetAnalysis').load(pathToApplicationOnServer+'forms/addDatasetAnalysisForm.html');
}

function checkLocalStorage() {
    if (localStorage.getItem("EditDatasetAnalysisScs")) {
        $('#alertMessage').append('<div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Well done!</strong> You successfully edit chosen dataset analysis </div>');
        localStorage.clear();

    }
    if (localStorage.getItem("DeleteDatasetAnalysisScs")) {
        $('#alertMessage').append('<div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Well done!</strong> You successfully delete chosen dataset analysis.</div>');
        localStorage.clear();
    }

    if (localStorage.getItem("EditDatasetScs")) {

        $('#alertMessage').append('<div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Well done!</strong> You successfully edit chosen dataset </div>');
        localStorage.clear();
    }
    if (localStorage.getItem("DeleteDatasetScs")) {
        $('#alertMessage').append('<div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Well done!</strong> You successfully delete chosen dataset.</div>');
        localStorage.clear();
    }
    if (localStorage.getItem("AddDatasetAnalysisScs")) {
        $('#alertMessage').append('<div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Well done!</strong> You successfully add dataset analysis </div>');
        localStorage.clear();
    }
    if (localStorage.getItem("AddDatasetScs")) {
        $('#alertMessage').append('<div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Well done!</strong> You successfully add dataset </div>');
        localStorage.clear();
    }
}



$('#addDatasetAnalysis').on('show.bs.modal', function (event) {
    $('#addDAStep1').show();
    $('#addDAstep2').hide();

});

$('#insertDataset').on('show.bs.modal', function (event) {
    $('#addDatasetStep1').show();
    $('#addDatasetStep2').hide();
});

// -------------------------------------------------------------------- searching ----------------------------------------------------------

$('body').on('click', 'button.searchDatasetBtn', function () {

    var datasetAnalysis = (this).getAttribute('analysis-id');
    var searchInput = $('#searchInput' + datasetAnalysis).val();
    search(searchInput);


});

$('body').on('keyup', 'input.searchDatasetInput', function () {
    var datasetAnalysis = (this).getAttribute('analysis-id');
    if (event.keyCode == 13) {
        $("#searchBtn" + datasetAnalysis).click();
    }
});

$("#searchInput").keyup(function (event) {
    if (event.keyCode == 13) {
        $("#searchBtn").click();
    }
});

function search(input) {
    var resultsDomains = [];
    var resultsEntities = [];

    var searchValue = input;

    $.each(entityList, function (index, entity) {
        var founded = entity.path.search(new RegExp(searchValue, "i"));
        if (founded >= 0) {
            resultsEntities.push(entity);
        }
    });
    $.each(domainList, function (index, domain) {
        var founded = domain.name.search(new RegExp(searchValue, "i"));
        if (founded >= 0) {
            resultsDomains.push(domain);
        }
    });

    if (resultsDomains.length > 0 || resultsEntities.length > 0) {
        $('.domainTitle').hide();
        $('.entityTitle').hide();
        $('.predicateTitle').hide();


    }
    if (resultsDomains.length > 0) {
        $.each(resultsDomains, function (index, domain) {
            $('tr.domainTitleId' + domain.id).show();
            if ($('tr.domainTitleId' + domain.id).children().children().hasClass("glyphicon-minus")) {
                $('tr.domainTitleId' + domain.id).children().children().removeClass("glyphicon-minus").addClass("glyphicon-plus");
            }

        })
    }

    if (resultsEntities.length > 0) {
        $.each(resultsEntities, function (index, entity) {
            $('tr.domainTitleId' + entity.domain.id).show();

        })
    }
}

//---------------------------------------------------------chart --------------------------------------------------------

function createVisualization(analysisId, wi, hi) {

    var datasetAnalysis = getDatasetAnalysis(analysisId);


    if (actualDataForDatasetChart.size != 0) {
        if (datasetAnalysis.entityCount != 0) {
            var b = {
                w: wi / 3, h: 30, s: 3, t: 10
            };
            var width = wi,
                height = hi,
                radius = (Math.min(width, height) / 2) - 10;

            var x = d3.scale.linear()
                .range([0, 2 * Math.PI]);

            var y = d3.scale.sqrt()
                .range([0, radius]);

            var color = d3.scale.category20c();

            var svg = d3.select("#chart" + analysisId).append("svg:svg")
                .attr("width", width)
                .attr("height", height)
                .append("g")
                .attr("transform", "translate(" + width / 2 + "," + (height / 2 + 10) + ")");

            var partition = d3.layout.partition()
                .sort(null)
                .value(function (d) {
                    return d.size;
                });

            var arc = d3.svg.arc()
                .startAngle(function (d) {
                    return Math.max(0, Math.min(2 * Math.PI, x(d.x)));
                })
                .endAngle(function (d) {
                    return Math.max(0, Math.min(2 * Math.PI, x(d.x + d.dx)));
                })
                .innerRadius(function (d) {
                    return Math.max(0, y(d.y));
                })
                .outerRadius(function (d) {
                    return Math.max(0, y(d.y + d.dy));
                });

// Keep track of the node that is currently being displayed as the root.
            var node;
            var root = actualDataForDatasetChart;


        }
        initializeBreadcrumbTrail();

        node = root;
        var path = svg.datum(root).selectAll("path")
            .data(partition.nodes)
            .enter().append("path")
            .attr("d", arc)
            .attr("data-toggle", "tooltip")
            .text("ddddd")
            .style("fill", function (d) {
                return color((d.children ? d : d.parent).name);
            })
            .on("click", click)
            .on("mouseover", mouseover)
            .each(stash);

        var totalSize = path.node().__data__.size;
        d3.selectAll("input").on("change", function change() {
            var value = this.value === "count"
                ? function () {
                return 1;
            }
                : function (d) {
                return d.size;
            };

            path
                .data(partition.value(value).nodes)
                .transition()
                .duration(1000)
                .attrTween("d", arcTweenData);
        });

        function click(d) {
            node = d;
            path.transition()
                .duration(1000)
                .attrTween("d", arcTweenZoom(d));
        }

        function mouseover(d) {


            $(this).tooltip();
            var r = d3.select(this).node().getBoundingClientRect();
            var percentage = (100 * d.size / totalSize).toPrecision(3);
            var percentageString = percentage + "%";
            if (percentage < 0.1) {
                percentageString = "< 0.1%";
            }
            if (percentage == 100) {
                percentageString = "";
            }

            d3.select("#percentage")
                .text(percentageString);
            var sequenceArray = getAncestors(d);
            updateBreadcrumbs(sequenceArray, percentageString);


            d3.selectAll("path")
                .style("opacity", 1);


            // Then highlight only those that are an ancestor of the current segment.
            svg.selectAll("path")
                .filter(function (node) {
                    return (sequenceArray.indexOf(node) >= 0);
                })
                .style("opacity", 0.5);


        }


        function initializeBreadcrumbTrail() {
            // Add the svg area.
            var trail = d3.select("#sequence" + analysisId).append("svg:svg")
                .attr("width", width)
                .attr("height", 50)
                .attr("id", "trail");
            // Add the label at the end, for the percentage.
            trail.append("svg:text")
                .attr("id", "endlabel")
                .style("fill", "#000");
        }

        function breadcrumbPoints(d, i) {
            var points = [];
            points.push("0,0");
            points.push(b.w + ",0");
            points.push(b.w + b.t + "," + (b.h / 2));
            points.push(b.w + "," + b.h);
            points.push("0," + b.h);
            if (i > 0) { // Leftmost breadcrumb; don't include 6th vertex.
                points.push(b.t + "," + (b.h / 2));
            }
            return points.join(" ");
        }

        function getShortNameOfEntity(name) {
            var nameParsed = name.split("/");
            return nameParsed[nameParsed.length - 1];
        }

        function updateBreadcrumbs(nodeArray, percentageString) {

            // Data join; key function combines name and depth (= position in sequence).
            var g = d3.select("#trail")
                .selectAll("g")
                .data(nodeArray, function (d) {
                    return d.name + d.depth;
                });

            // Add breadcrumb and label for entering nodes.
            var entering = g.enter().append("svg:g");

            entering.append("svg:polygon")
                .attr("points", breadcrumbPoints)
                .style("fill", function (d) {
                    return color((d.children ? d : d.parent).name);
                });


            entering.append("svg:text")
                .attr("x", (b.w + b.t) / 2)
                .attr("y", b.h / 2)
                .attr("dy", "0.35em")
                .attr("text-anchor", "middle")
                .text(function (d) {
                    return getShortNameOfEntity(d.name);
                });

            // Set position for entering and updating nodes.
            g.attr("transform", function (d, i) {
                return "translate(" + i * (b.w + b.s) + ", 0)";
            });

            // Remove exiting nodes.
            g.exit().remove();

            // Now move and update the percentage at the end.
            d3.select("#trail").select("#endlabel")
                .attr("x", (nodeArray.length + 0.5) * (b.w + b.s))
                .attr("y", b.h / 2)
                .attr("dy", "0.35em")
                .attr("text-anchor", "middle")
                .text(percentageString);

            // Make the breadcrumb trail visible, if it's hidden.
            d3.select("#trail")
                .style("visibility", "");

        }

        function getAncestors(node) {
            var path = [];
            var current = node;
            while (current.parent) {
                path.unshift(current);
                current = current.parent;
            }
            return path;
        }

        d3.select(self.frameElement).style("height", height + "px");

// Setup for switching data: stash the old values for transition.
        function stash(d) {
            d.x0 = d.x;
            d.dx0 = d.dx;
        }

// When switching data: interpolate the arcs in data space.
        function arcTweenData(a, i) {
            var oi = d3.interpolate({x: a.x0, dx: a.dx0}, a);

            function tween(t) {
                var b = oi(t);
                a.x0 = b.x;
                a.dx0 = b.dx;
                return arc(b);
            }

            if (i == 0) {
                // If we are on the first arc, adjust the x domain to match the root node
                // at the current zoom level. (We only need to do this once.)
                var xd = d3.interpolate(x.domain(), [node.x, node.x + node.dx]);
                return function (t) {
                    x.domain(xd(t));
                    return tween(t);
                };
            } else {
                return tween;
            }
        }

// When zooming: interpolate the scales.
        function arcTweenZoom(d) {
            var xd = d3.interpolate(x.domain(), [d.x, d.x + d.dx]),
                yd = d3.interpolate(y.domain(), [d.y, 1]),
                yr = d3.interpolate(y.range(), [d.y ? 20 : 0, radius]);
            return function (d, i) {
                return i
                    ? function (t) {
                    return arc(d);
                }
                    : function (t) {
                    x.domain(xd(t));
                    y.domain(yd(t)).range(yr(t));
                    return arc(d);
                };
            };
        }
    }
}



