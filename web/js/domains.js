/**
 * Created by Janka on 10/2/2016.
 * This script is used with HTML page domains.html
 */

var rootUrl = 'http://localhost:8080/rest/restMethod';
var domainList;
var entityList;
var entityGroupList;
var domainChart;
var dataForChart;

$(document).ready(function () {
    checkLocalStorage();
    loadForms();
    loadDomainToPanel();
    loadEntitiesToDomainsList();
    loadDomainChart();
    setSizeBtnGroupTitle();
    removeTmpFiles();
});

//---------------------------------------------------------------- validation functions --------------------------------------------------------------------------

function validateUniquationOfDomainName(inputName, domainId) {

    var actualName = "";
    if (domainId != "") {
        actualName = getDomainName(domainId);
    }
    var result = 0;
    $.each(domainList, function (index, domain) {
        if (domain.name === inputName && inputName != actualName) {
            result = 1;
        }
    });
    if (result == 0) {
        return true
    } else {
        return false;
    }

}

function validateUniquationOfDomainPath(inputName, domainId) {

    var actualPath = "";
    if (domainId != "") {
        actualPath = getDomainPath(domainId);
    }

    var result = 0;
    $.each(domainList, function (index, domain) {
        if (domain.path === inputName && inputName != actualPath) {
            result = 1;
        }
    });
    if (result == 0) {
        return true
    } else {
        return false;
    }

}

function validateUniquationOfEntityPath(inputName, entityId) {
    var actualPath = "";
    if (entityId != "") {
        actualPath = getEntityPath(entityId);
    }
    var result = 0;
    $.each(entityList, function (index, entity) {
        if (entity.path === inputName && inputName != actualPath) {
            result = 1;
        }
    });
    if (result == 0) {
        return true
    } else {
        return false;
    }

}

//--------------------------------------------helpful methods----------------------------------

function getDomainName(id) {
    var domainName = "";
    $.each(domainList, function (index, domain) {
        if (domain.id == id) {
            domainName = domain.name;
        }
    });
    return domainName;
}

function getDomainPath(id) {
    var domainPath = "";
    $.each(domainList, function (index, domain) {
        if (domain.id == id) {
            domainPath = domain.path;
        }
    });
    return domainPath;
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

function fillComboboxForAddEntity(data) {

    var select = document.getElementById("entityGroupCmb");
    $('#entityGroupCmb')
        .find('option')
        .remove()
        .end()
    ;
    $.each(data, function (index, entityGroup) {
        var option = document.createElement('option');
        option.text = entityGroup.name;
        option.value = entityGroup.id;
        select.add(option, 0);
    });
    var option = document.createElement('option');
    option.setAttribute("selected", "selected");
    option.text = "";
    option.value = 0;
    select.add(option, 0);

}

//--------------------------------- on resize ----------------------

$(window).resize(function () {
    setSizeBtnGroupTitle();
    setDomainBySize();
    setPaddingInPanel();

    if ($('#chart').length > 0) {
        $('#chart').empty();
        drawChart($('.whiteWithoutPadding').width(), $('.whiteWithoutPadding').width() * 0.6);
    }

});

// -------------------------------- action on click -----------------------------
function openEditDomain(domainId) {
    var domainName = getDomainName(domainId);
    var domainPath = getDomainPath(domainId);
    $("h4.editDomainTitle").text("Edit domain: \"" + domainName + "\" !");
    document.getElementById("domainName").setAttribute("value", domainName);
    document.getElementById("domainName").setAttribute("data-parsley-uniqueDomainName", domainId);
    document.getElementById("domainPath").setAttribute("value", domainPath);
    document.getElementById("domainPath").setAttribute("data-parsley-uniqueDomainPath", domainId);
    document.getElementById("btnEditDomainSummit").setAttribute("domain-identity", domainId);
    $("#editDomain").modal('show');
}

function openDeleteDomain(domainId) {
    var domainName = getDomainName(domainId);
    $("h4.deleteDomainTitle").text("Delete domain: \"" + domainName + "\" !");
    document.getElementById("btnDeleteDomainSummit").setAttribute("domain-identity", domainId);
    $("#deleteDomain").modal('show');

}

function openAddEntity(domainId) {

    var domainName = getDomainName(domainId);
    $("h4.addEntityTitle").text("Add entity to domain: \"" + domainName + "\" !");
    document.getElementById("btnAddEntitySummit").setAttribute("domain-identity", domainId);
    findEntitiesGroupInDomain(domainId);
    $("#addEntity").modal('show');

}

$('body').on('click', 'button.btnAddEntity', function () {

});

$('body').on('click', 'button.btnAddDomain', function () {
    document.getElementById("addedDomainPath").setAttribute("value", "http://mydomains.com/domains");
    $("#addDomain").modal('show');

});

$("#domainsChart").click(
    function (evt) {
        var activePoints = domainChart.getElementsAtEvent(evt);
        var url = "http://example.com/?label=" + activePoints[0].label + "&value=" + activePoints[0].value;
        alert(url);
    }
);

$('body').on('click', 'button.btnEditEntity', function () {
    var entityPath = getEntityPath($(this).attr("entity-identity"));
    $("h4.editEntityTitle").text("Edit entity: \"" + entityPath + "\"");
    document.getElementById("entityNameForEdit").setAttribute("value", entityPath);
    document.getElementById("entityNameForEdit").setAttribute("data-parsley-uniqueEntityPath", $(this).attr("entity-identity"));
    document.getElementById("btnEditEntitySummit").setAttribute("entity-identity", $(this).attr("entity-identity"));

    $("#editEntity").modal('show');

});

$('body').on('click', 'button.btnDeleteEntity', function () {
    var entityPath = getEntityPath($(this).attr("entity-identity"));
    $("h4.deleteEntityTitle").text("Delete entity: \"" + entityPath + "\" !");
    document.getElementById("btnDeleteEntitySummit").setAttribute("entity-identity", $(this).attr("entity-identity"));
    $("#deleteEntity").modal('show');

});

$('body').on('click', 'button#btnImportDomains', function () {

    $("#importDomain").modal('show');

});

//-----------------------------------------------------------------------------searching ----------------------------------------------------------

$('body').on('click', 'button#searchBtn', function () {

    var searchInput = $('#searchInput').val();
    search(searchInput);


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
        $('.panel').hide();
        $('.in').toggle();


    }
    if (resultsDomains.length > 0) {
        $.each(resultsDomains, function (index, domain) {
                $('#panel' + domain.id).show();
                if (resultsDomains.length == 1) {
                    if (resultsEntities.length == 0 || ( resultsEntities.length == 1 && resultsEntities[0].domain.id == domain.id)) {
                        $('#collapse-domain' + domain.id).collapse();
                    }

                }
            }
        );
    }

    if (resultsEntities.length > 0) {
        $.each(resultsEntities, function (index, entity) {
            $('#panel' + entity.domain.id).show();
            if (resultsEntities.length == 1) {
                if (resultsDomains.length == 0 || (resultsDomains.length == 1 && resultsDomains[0].id == entity.domain.id)) {
                    $('#collapse-domain' + entity.domain.id).collapse();
                }


            }
        })
    }
}

//----------------------------------------submit form--------------------------------
//used in modal
function editDomainFunction() {

    var domainId = $('input#btnEditDomainSummit').attr("domain-identity");
    var input = editDomainFormToJSON();
    $("#editDomain").modal('hide');
    editDomain(domainId, input);

}
$('body').on('click', 'button#btnDeleteDomainSummit', function () {

    var domainId = $(this).attr("domain-identity");
    $("#deleteDomain").modal('hide');
    deleteDomain(domainId);

});

//used in modal
function addEntityFunction() {
    var domainId = $('input#btnAddEntitySummit').attr("domain-identity");
    $("#addEntity").modal('hide');
    var selectedEntityGroupId = $('#entityGroupCmb').val();
    var data = entityFormToJSON();
    addEntity(domainId, selectedEntityGroupId, data);
}

$('body').on('change', 'input#addedDomainName', function () {
    $('#formAddDomain').parsley().validate();
});

$('body').on('change', 'input#addedDomainPath', function () {
    $('#formAddDomain').parsley().validate();
});

$('body').on('change', 'input#fileImported', function () {
    $('#formImportDomain').parsley().validate();
});

$('body').on('change', 'input#domainName', function () {
    $('#formEditDomain').parsley().validate();
});

$('body').on('change', 'input#domainPath', function () {
    $('#formEditDomain').parsley().validate();
});

$('body').on('change', 'input#entityName', function () {
    $('#formAddEntity').parsley().validate();
});

$('body').on('change', 'input#entityNameForEdit', function () {
    $('#formEditEntity').parsley().validate();
});

$('body').on('change', 'input#addedDomainName', function () {
    var inputValueArray = $(this).val().split(" ");
    var returnValue = "";
    $.each(inputValueArray, function (index, inputValue) {
        returnValue += (inputValue.charAt(0).toUpperCase() + inputValue.slice(1));
    });
    document.getElementById("addedDomainPath").setAttribute("value", "http://mydomains.com/domains/" + returnValue);
    $('#formAddDomain').parsley().validate();
});

$('body').on('change', 'input#domainName', function () {
    var inputValueArray = $(this).val().split(" ");
    var returnValue = "";
    $.each(inputValueArray, function (index, inputValue) {
        returnValue += (inputValue.charAt(0).toUpperCase() + inputValue.slice(1));
    });
    document.getElementById("domainPath").setAttribute("value", "http://mydomains.com/domains/" + returnValue);
    $('#formEditDomain').parsley().validate();
});

//used in modal
function addNewDomain() {
    $("#addDomain").modal('hide');
    var input = domainFormToJSON();
    addDomain(input);
}

//used in modal
function editEntityFunction() {
    var entityId = $('input#btnEditEntitySummit').attr("entity-identity");
    var input = $('#entityNameForEdit').val();
    $("#editEntityt").modal('hide');
    editEntity(entityId, input);

}

$('body').on('click', 'button#btnDeleteEntitySummit', function () {

    var entityId = $(this).attr("entity-identity");
    $("#deleteEntity").modal('hide');
    deleteEntity(entityId);

});

//used in modal
function importDomains() {

    var file = document.getElementById('fileImported').files[0]; //Files[0] = 1st file
    var reader = new FileReader();
    reader.readAsText(file, 'UTF-8');
    var contentOfFile;
    reader.onload = function (e) {
        contentOfFile = e.target.result;
        importDomains2(contentOfFile);
    };
}
//})

//-----------------------------------------------------------loading elements - window size ---------------------------------------------------------------------------------

function setDomainBySize() {

    if ($(window).width() < 500) {
        $(".panelDomainBigger").hide();
        $(".panelDomainSmaller").show();

    } else {

        $(".panelDomainSmaller").hide();
        $(".panelDomainBigger").show();
    }
}

function setPaddingInPanel() {
    if ($(window).width() < 992) {
        $(".panelColTitle").removeClass("noLeftPadding");
        $(".panelColBtn").removeClass("noRightPadding");

    } else {
        $(".panelColTitle").addClass("noLeftPadding");
        $(".panelColBtn").addClass("noRightPadding")
    }
}

function setSizeBtnGroupTitle() {
    if ($(window).width() > 991) {
        $('.domainTitleBtnGroup').addClass('btn-group-md').removeClass('btn-group-sm').removeClass('btn-group-xs');
    } else if ($(window).width() > 767) {
        $('.domainTitleBtnGroup').addClass('btn-group-sm').removeClass('btn-group-md').removeClass('btn-group-xs');
    } else if ($(window).width() > 500) {
        $('.domainTitleBtnGroup').addClass('btn-group-xs').removeClass('btn-group-md').removeClass('btn-group-sm');
    }
}

//--------------------------------- loading data to html tags ----------------------------------

function loadDomainToPanel() {

    findAllDomains();
    $.each(domainList, function (index, domain) {

        $('#domainListPanel').append('<div class="panel panel-default" id="panel' + domain.id + '"></div>');
        $('#panel' + domain.id).append('<div class="panel-heading" role="tab" id="headingDomain' + domain.id + '">');
        $('#headingDomain' + domain.id).append('<div class="row panelDomainBigger"><div class="leftPadding col-md-8 col-sm-8 col-xs-6"><h4 class="panel-title">' +
            '<a role="button" class="collapsed" data-toggle="collapse" data-parent="#domainListPanel" href="#collapse-domain' + domain.id + '" data-identity="' + domain.id + '" aria-expanded="false" aria-controls="collapse-domain' + domain.id + '">' + domain.name + '</a>' +
            '</h4></div> <div class="rightPadding col-md-4 col-sm-4 col-xs-6"> <div class="btn-group btn-group-xs" role="group" aria-label="...">' +
            '<button onclick="openEditDomain(' + domain.id + ')" type="button" class="btn btn-default btnEditDomain" id="btnEditDomain' + domain.id + '" domain-identity="' + domain.id + '"> Edit</button>' +
            '<button onclick="openDeleteDomain(' + domain.id + ')" type="button" class="btn btn-default btnDeleteDomain" id="btnDeleteDomain' + domain.id + '" domain-identity="' + domain.id + '">Delete</button>' +
            '<button onclick="openAddEntity(' + domain.id + ')" type="button" class="btn btn-default btnAddEntity" id="btnAddEntity' + domain.id + '" domain-identity="' + domain.id + '">Add entity</button></div></div></div>');

        $('#headingDomain' + domain.id).append('<div class="row panelDomainSmaller"><div class="leftPadding col-md-8 col-sm-8 col-xs-6"><h4 class="panel-title">' +
            '<a role="button" class="collapsed" data-toggle="collapse" data-parent="#domainListPanel" href="#collapse-domain' + domain.id + '" data-identity="' + domain.id + '" aria-expanded="false" aria-controls="collapse-domain' + domain.id + '">' + domain.name + '</a>' +
            '</h4></div> <div class="rightPadding col-md-4 col-sm-4 col-xs-6 text-right"> <div class="btn-group-xs smalletBtnGroupDataset" role="group" aria-label="..."><div class="btn-group-xs role="group">' +
            '<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">' +
            '<span class="caret"></span> </button> <ul class="dropdown-menu dropdown-menu-right">' +
            '<li><a href="#" onclick="openEditDomain(' + domain.id + ')" class="btnEditDomain" id="btnEditDomain' + domain.id + '" domain-identity="' + domain.id + '">Edit</a></li>' +
            '<li><a href="#" onclick="openDeleteDomain(' + domain.id + ')" class="btnDeleteDomain" id="btnDeleteDomain' + domain.id + '" domain-identity="' + domain.id + '">Delete</a></li>' +
            '<li><a href="#" onclick="openAddEntity(' + domain.id + ')" class="btnAddEntity" id="btnAddEntity' + domain.id + '" domain-identity="' + domain.id + '">Add entity</a></li>' +
            '</ul> </div></div></div></div>');

        setDomainBySize();
        setPaddingInPanel();
        $('#panel' + domain.id).append('<div id="collapse-domain' + domain.id + '" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingDomain' + domain.id + '">');
        $('#collapse-domain' + domain.id).append('<div class="panel-body"> <table class="tableDomain table table-bordered table-hover" id="tableDomain' + domain.id + '"><tr><th>Id</th><th>Path</th></tr></table> ');

    })
}

function loadEntitiesToDomainsList() {
    findAllEntities();
    findAllEntitiesGroup();
    $.each(entityList, function (index, entity) {
        loadEntityToDomainTable(entity);
    })
}

function loadEntityToDomainTable(entity) {
    var domainId = entity.domain.id;
    $('#tableDomain' + domainId).append('<tr><th scope="row">' + entity.id + '</th><td>' + entity.path + '<div class="btn-group-xs" role="group" aria-label="...">' +
        '<button type="button" class="btn btn-xs btn-default btnEditEntity" entity-identity="' + entity.id + '" id="btnEditEntity' + entity.id + '">Edit</button>' +
        '<button type="button" class="btn btn-xs btn-default btnDeleteEntity" entity-identity="' + entity.id + '" id="btnDeleteEntity' + entity.id + '">Delete</button>' +
        '</div></td></tr>');

}

function loadDomainChart() {
    getDataForDomainChart();
}

//----------------------------- get data ------------------------------------

function findAllDomains() {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/getDomains",
        dataType: "json",
        async: false,
        success: function (data) {
            domainList = data;
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

function findAllEntitiesGroup() {
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

function findEntitiesGroupInDomain(domain_id) {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/getEntityGroupsAccordingToDomain/" + domain_id,
        dataType: "json",
        async: false,
        success: function (data) {
            fillComboboxForAddEntity(data);
        }
    })
}

function getDataForDomainChart() {
    $.ajax({
        type: 'GET',
        url: rootUrl + "/getDataForDomainChart",
        dataType: "json",
        async: false,
        success: function (data) {
            var wi = $('.whiteWithoutPadding').width();
            var hi = (0.6 * $('.whiteWithoutPadding').width());
            dataForChart = data;
            drawChart(wi, hi);
        },
        error: function () {
        }

    })
}

//-----------------------------------------edit data -----------------------

function editDomain(domainId, input) {
    $.ajax({
        type: 'POST',
        url: rootUrl + "/updateDomain/" + domainId,
        dataType: 'json',
        contentType: 'application/json',
        data: input,
        async: false,
        success: function (data) {
            var status = "successful";
            localStorage.setItem("EditDomainScs", status);
            window.location.reload(true);
        },
        error: function (data2) {
            $('#alertMessage').append('<div class="alert alert-danger alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Error!</strong>Domain editing was not successfull.</div>');
        }
    })
}

function editEntity(entityId, entityPath) {
    $.ajax({
        type: 'POST',
        url: rootUrl + "/updateEntity/" + entityId,
        dataType: "text",
        contentType: 'text/plain',
        accept: 'text/plain',
        data: entityPath,
        async: false,
        success: function (data) {
            var status = "successful";
            localStorage.setItem("EditEntityScs", status);
            window.location.reload(true);
        },
        error: function (data2) {
            $('#alertMessage').append('<div class="alert alert-danger alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Error! </strong> Entity editing was not successfull.</div>');
        }
    })
}

//------------------------------------------delete data ------------------------------

function deleteDomain(domainId) {
    $.ajax({
        type: 'DELETE',
        url: rootUrl + "/deleteDomain/" + domainId,
        async: false,
        success: function () {
            var status = "successful";
            localStorage.setItem("AddDomainScs", status);
            window.location.reload(true);
        },
        error: function (data) {
            alert("error");
        }
    })
}

function deleteEntity(entityId) {
    $.ajax({
        type: 'DELETE',
        url: rootUrl + "/deleteEntity/" + entityId,
        async: false,
        success: function () {
            var status = "successful";
            localStorage.setItem("DeleteEntityScs", status);
            window.location.reload(true);
        },
        error: function (data) {
            $('#alertMessage').append('<div class="alert alert-danger alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Error!</strong>Entity deleting was not successfull.</div>');

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

function addEntity(domainId, entityGroupId, data) {
    $.ajax({
        type: 'POST',
        url: rootUrl + "/createEntity/domain/" + domainId + "/entityGroup/" + entityGroupId,
        dataType: 'json',
        contentType: 'application/json',
        accept: 'application/json',
        data: data,
        async: false,
        success: function (data2) {
            var status = "successful";
            localStorage.setItem("AddEntityScs", status);
            window.location.reload(true);
        },
        error: function (data2) {
            $('#alertMessage').append('<div class="alert alert-danger alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Error!</strong>Entity adding was not successfull.</div>');


        }
    })
}

function addDomain(data) {
    $.ajax({
        type: 'POST',
        url: rootUrl + "/createDomain",
        dataType: 'json',
        contentType: 'application/json',
        accept: 'application/json',
        data: data,
        async: false,
        success: function (data2) {
            //loadAddedDomainToPanel(data2);
            var status = "successful";
            localStorage.setItem("AddDomainScs", status);
            window.location.reload(true);
        },
        error: function (data2) {
            $('#alertMessage').append('<div class="alert alert-danger alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Error!</strong>Domain adding was not successfull.</div>');
        }
    })
}

function importDomains2(data) {
    $.ajax({
        type: 'POST',
        url: rootUrl + "/inportDomains2",
        dataType: "text",
        contentType: 'text/plain',
        accept: 'text/plain',
        data: data,
        async: false,
        success: function (data) {
            var status = "successful";
            localStorage.setItem("ImportScs", status);
            window.location.reload(true);
        },
        error: function (data2) {
            $('#alertMessage').append('<div class="alert alert-danger alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Error!</strong>Domain importing was not successfull. ' + data + '</div>');
        }
    })
}

//--------------------------------------transform to jsob object ---------------------------

function entityFormToJSON() {
    var entityName = $('#entityName').val();
    return JSON.stringify({
        "id": null,
        "name": "",
        "path": entityName
    });

}

function domainFormToJSON() {
    var domainName = $('#addedDomainName').val();
    var domainPath = $('#addedDomainPath').val();
    return JSON.stringify({
        "id": null,
        "name": domainName,
        "path": domainPath
    });

}

function editDomainFormToJSON() {
    var domainName = $('#domainName').val();
    var domainPath = $('#domainPath').val();
    return JSON.stringify({
        "id": null,
        "name": domainName,
        "path": domainPath
    });

}

// ----------------------------------------------- load charts ---------------------------------------

function drawChart(wi, hi) {
    $('#chart').empty();

    var data = dataForChart;
    var width = wi,
        height = hi;

    var color = d3.scale.category20();

    var outerRadius = height / 2 - 10,
        innerRadius = outerRadius / 5,
        cornerRadius = 20;

    var pie = d3.layout.pie()
        .value(function (d) {
            return d.countOfEntities;
        });

    var arc = d3.svg.arc()
        .padRadius(outerRadius)
        .innerRadius(innerRadius);

    var svg = d3.select("#chart").append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
        .attr("transform", "translate(" + width / 2 + "," + height / 2 + ")");
    var totalCount = 0;


    for (var i = 0; i < data.length; i++) {
        totalCount += pie(data)[i].value;

    }
    var path = svg.selectAll("path")
        .data(pie(data))

        .enter().append("path")
        .each(function (d) {
            d.outerRadius = outerRadius - 20;
        })
        .attr("fill", function (d, i) {
            return color(i);
        })
        .attr("d", arc)
        .attr("label", function (d) {
            return d.data.domainName;
        })
        .attr("data", function (d) {
            return d.data.countOfEntities
        })
        .on("mouseover", arcTween(outerRadius, 0))
        .on("mouseout", arcTween(outerRadius - 20, 150));


    function arcTween(outerRadius, delay) {

        return function () {

            if (delay == 0) {
                d3.select(this)
                    .style("opacity", 0.5);
                var r = d3.select(this).node().getBoundingClientRect();
                var position = $(this).position();
                d3.select("div#tooltip")
                    .style("display", "inline")
                    .style("top", (position.top + r.height / 2) + "px")
                    .style("left", (position.left + r.width / 2) + "px")
                    .style("position", "absolute")
                    .text(d3.select(this).attr("label") + " " + Math.round(d3.select(this).attr("data") / totalCount * 100) + "%");
                // .text(d3.select(this).attr("data"));
            } else {
                d3.select(this)
                    .style("opacity", 1);
                d3.select("div#tooltip")
                    .style("display", "none")
            }
            d3.select(this).transition().delay(delay).attrTween("d", function (d) {
                var i = d3.interpolate(d.outerRadius, outerRadius);
                return function (t) {
                    d.outerRadius = i(t);
                    return arc(d);
                };
            });
        };
    }
}

//load modals
function loadForms() {
    $('#editDomain').load('http://localhost:8080/forms/editDomainForm.html');
    $('#deleteDomain').load('http://localhost:8080/forms/deleteDomainForm.html');
    $('#addEntity').load('http://localhost:8080/forms/addEntityForm.html');
    $('#addDomain').load('http://localhost:8080/forms/addDomainForm.html');
    $('#editEntity').load('http://localhost:8080/forms/editEntityForm.html');
    $('#deleteEntity').load('http://localhost:8080/forms/deleteEntityForm.html');
    $('#importDomain').load('http://localhost:8080/forms/importDomainForm.html');
}

function checkLocalStorage() {
    if (localStorage.getItem("EditEntityScs")) {
        $('#alertMessage').append('<div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Well done!</strong> You successfully edit chosen entity </div>');
        localStorage.clear();

    }
    if (localStorage.getItem("DeleteEntityScs")) {
        $('#alertMessage').append('<div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Well done!</strong> You successfully delete chosen entity.</div>');
        localStorage.clear();
    }

    if (localStorage.getItem("EditDomainScs")) {

        $('#alertMessage').append('<div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Well done!</strong> You successfully edit chosen domain </div>');
        localStorage.clear();
    }
    if (localStorage.getItem("DeleteDomainScs")) {
        $('#alertMessage').append('<div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Well done!</strong> You successfully delete chosen domain.</div>');
        localStorage.clear();
    }
    if (localStorage.getItem("AddEntityScs")) {
        $('#alertMessage').append('<div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Well done!</strong> You successfully add entity </div>');
        localStorage.clear();
    }
    if (localStorage.getItem("AddDomainScs")) {
        $('#alertMessage').append('<div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Well done!</strong> You successfully add domain </div>');
        localStorage.clear();
    }

    if (localStorage.getItem("ImportScs")) {
        $('#alertMessage').append('<div class="alert alert-success alert-dismissible" role="alert"> <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button> <strong>Well done!</strong> You successfully import domains and entities </div>');
        localStorage.clear();
    }

}



    
 
    
