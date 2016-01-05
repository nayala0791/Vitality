<?php
$response = array();
if (isset($_GET['userId']) && isset($_GET['hospitalId'])) {
    $userId = $_GET['userId'];
    $hospitalId = $_GET['hospitalId'];
    $query = "Select * from device_info where userId='$userId'";
    $hospitalSelectQuery = "Select * from hospital where user_id='$hospitalId'";
    $date = date("Y-m-d h:i:sa");
    $hospitalToDonorTransaction =
        "Insert into hospital_donar_request (`hospital_id`,`user_id`,`requested_dateTime`) VALUES ('$hospitalId','$userId','$date')";
    require_once __dir__ . '/db_config.php';
    include_once __dir__ . '/PushNotification.php';
    $con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE) or die("Error Connecting Database " .
        mysqli_connect_error($con));

    $result = mysqli_query($con, $query);


    if (mysqli_num_rows($result) > 0) {

        $donorDetails = mysqli_fetch_array($result);

        $regId = $donorDetails['deviceRegistrationId'];
        $gcm = new GCM();
        $registatoin_ids = array($regId);
        $hospitalQueryResult = mysqli_query($con, $hospitalSelectQuery);

        if (mysqli_num_rows($hospitalQueryResult) > 0) {

            // Insert the request in Data table
            $insertIntoRequest = mysqli_query($con,$hospitalToDonorTransaction);
            echo mysqli_affected_rows($con);
            if (mysqli_affected_rows($con) > 0) {
                // Construct the push notification object
                
                $transactionId=mysqli_insert_id($con);

                $hospitalDetails=mysqli_fetch_array($hospitalQueryResult);
                
                $hospitalDetailInJson=array();
                
                $hospitalDetailInJson["transactionId"]=$transactionId;
                $hospitalDetailInJson["hospitalId"]=$hospitalDetails["user_id"];
                $hospitalDetailInJson["hospitalName"]=$hospitalDetails["hospital_name"];
                $hospitalDetailInJson["hospitalAddress"]=$hospitalDetails["hospital_address"];
                $hospitalDetailInJson["speciality"]=$hospitalDetails["speciality"];
                $hospitalDetailInJson["phoneNumber"]=$hospitalDetails["phone_number"];
                
                $message = array("message" => json_encode($hospitalDetailInJson));

                $result = $gcm->send_notification($registatoin_ids, $message);
                $responseInJson = json_decode($result, true);

                if ($responseInJson[success] == 1) {
                    $response["isRequestSent"] = 1;
                    $response["message"] = "Request sent to the donor";
                    echo json_encode($response);
                } else {
                    $response["isRequestSent"] = 0;
                    $response["message"] = "Request Not Sent .Delivary Failed!";
                    echo json_encode($response);
                }
            }else{
                
            $response["isRequestSent"] = 0;
            $response["message"] = "Request Not Sent.Transaction Entry Failure";
            echo json_encode($response);
            }
        } else {
            $response["isRequestSent"] = 0;
            $response["message"] = "Request Not Sent.Hospital Is Not found";
            echo json_encode($response);
        }


    } else {
        $response["isRequestSent"] = 0;
        $response["message"] = "Error in sending the request.User Not Found";
        echo json_encode($response);
    }


} else {
    $response["isRequestSent"] = 0;
    $response["message"] = "Required field(s) is missing";
    echo json_encode($response);
}



?>	