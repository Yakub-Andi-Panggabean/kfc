$('#assetListModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget) // Button that triggered the modal
    var productID = $(button).attr("data-product-id");
    var selectedUnit = $('#from_' + productID + ' option:selected').val();
    var roID = $('#inputRoId').val()
    $.get("/item/shipment/" + roID + "?product_id=" + productID + "&unit_id=" + selectedUnit, function (data) {
            var content = $($.parseHTML(data)).find(".modal-body").html();
            $('#assetListModal').find('.modal-body').html(content);
        }
    )
});

$('#verifyOrderDetail').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget)
    var roID = $(button).attr("data-verify-order-id");
    $.get("/asset/verification/" + roID, function (data) {
            var body = $($.parseHTML(data)).find(".modal-body").html();
            var footer = $($.parseHTML(data)).find(".modal-footer").html();
            $('#verifyOrderDetail').find('.modal-body').html(body);
            $('#verifyOrderDetail').find('.modal-footer').html(footer);
        }
    )
});


function removeAsset(caller, event) {
    event.preventDefault();
    var action = $(caller).attr("data-action");
    console.log(action)
    $.post(action, function (data) {
        location.reload();
    });
}