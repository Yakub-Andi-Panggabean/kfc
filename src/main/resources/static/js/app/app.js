$(document).ready(function () {
    $('.table').DataTable({
        "ordering": false
    });
});

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

$('#assetPOListModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget) // Button that triggered the modal
    var receiptID = $(button).attr("data-receipt-id");
    $.get("/order/purchase/" + receiptID, function (data) {
            var content = $($.parseHTML(data)).find(".modal-body").html();
            $('#assetPOListModal').find('.modal-body').html(content);
        }
    )
});

$('#assetTransferDetail').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget) // Button that triggered the modal
    var receiptID = $(button).attr("data-transfer-id");
    $.get("/modal/item/transfer/" + receiptID, function (data) {
            var content = $($.parseHTML(data)).find(".modal-body").html();
            $('#assetTransferDetail').find('.modal-body').html(content);
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

$('#notificationDetail').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget)
    var roID = $(button).attr("data-notification-id");
    $.get("/notification/" + roID, function (data) {
            var body = $($.parseHTML(data)).find(".modal-body").html();
            var footer = $($.parseHTML(data)).find(".modal-footer").html();
            $('#notificationDetail').find('.modal-body').html(body);
            $('#notificationDetail').find('.modal-footer').html(footer);
        }
    )
});

$('#notificationDetail').on('hide.bs.modal', function (event) {
    location.reload();
});


function removeAsset(caller, event) {
    event.preventDefault();
    var action = $(caller).attr("data-action");
    $.post(action, function (data) {
        location.reload();
    });
}


$('#assetAdjustmentLocation').on('change', function () {
    var path = $('option:selected', this).attr('data-unit-adjustment');
    location.href = path
});


function addRoItem(caller) {

    var productId = $(caller).attr("data-product-id");
    var qtyId = $(caller).attr("data-qty-id");
    var qtyVal = $(qtyId).val();

    if (qtyVal < 1) {
        alert('invalid qty');
        return;
    }

    var action = "/order/request/product/add/" + productId + "?qty=" + qtyVal;
    $.get(action, function (data) {
        location.reload();
    });
}


function addReceiptItem(caller) {
    var productId = $(caller).attr("data-product-id");
    var qtyId = $(caller).attr("data-qty-id");
    var qtyVal = $(qtyId).val();

    if (qtyVal < 1) {
        alert('invalid qty');
        return;
    }

    $(caller).append('<input type="hidden" id="qty" name="qty" value=' + qtyVal + '>');

    $.ajax({
        type: 'POST',
        url: $(caller).attr('action'),
        data: $(caller).serialize(),
        success: function (data) {
            location.reload();
        }
    });
}