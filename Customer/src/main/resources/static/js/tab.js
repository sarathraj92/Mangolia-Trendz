$(document).ready(function () {
    // Handle tab click event
    $('#address-tab').on('click', function (e) {
        e.preventDefault(); // Prevent default link behavior
        const tabId = $(this).attr('href'); // Get the target tab ID

        // Load address content using AJAX
        $.ajax({
            url: '/shop/address', // Your controller endpoint to fetch address content
            method: 'GET',
            success: function (data) {
                // Update the content of the target tab
                $(tabId + ' .address-content').html(data);
            },
            error: function (error) {
                console.error('Error loading address content:', error);
            }
        });
    });



    $('#orders-tab').on('click', function (e) {
        e.preventDefault(); // Prevent default link behavior
        const tabId2 = $(this).attr('href'); // Get the target tab ID

        // Load address content using AJAX
        $.ajax({
            url: '/shop/orders', // Your controller endpoint to fetch address content
            method: 'GET',
            success: function (data) {
                // Update the content of the target tab
                $(tabId2 + ' .orders').html(data);
            },
            error: function (error) {
                console.error('Error loading address content:', error);
            }
        });
    });

    $('#account-detail-tab').on('click', function (e) {
        e.preventDefault(); // Prevent default link behavior
        const tabId2 = $(this).attr('href'); // Get the target tab ID

        // Load address content using AJAX
        $.ajax({
            url: '/shop/account-details', // Your controller endpoint to fetch address content
            method: 'GET',
            success: function (data) {
                // Update the content of the target tab
                $(tabId2 + ' .account-details').html(data);
            },
            error: function (error) {
                console.error('Error loading address content:', error);
            }
        });
    });

});




