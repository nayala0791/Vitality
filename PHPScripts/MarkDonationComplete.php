<?php
$response = array();
if (isset($_GET['transactionId']) && isset($_GET['userAction'])) {
    $transactionId = $_GET['transactionId'];
    $userAction = $_GET['userAction'];
    $update_date = date("Y-m-d h:i:sa");

    $query = "Update hospital_donar_request SET is_donation_successfull=$userAction where id=$transactionId";

    require_once __dir__ . '/db_config.php';
    include_once __dir__ . '/PushNotification.php';
    $con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE) or die("Error Connecting Database " .
        mysqli_connect_error($con));

    $result = mysqli_query($con, $query);


    if (mysqli_affected_rows($con) > 0) {

        $selectDonorDeviceInfo =
            "Select * from device_info where userId in (Select user_id from hospital_donar_request where id='$transactionId');";
        $donorDeviceInfo = mysqli_query($con, $selectDonorDeviceInfo);

        if (mysqli_num_rows($donorDeviceInfo) > 0) {
            $donorDetails = mysqli_fetch_array($donorDeviceInfo);

            $regId = $donorDetails['deviceRegistrationId'];
            $regId = 'APA91bEYxt5TBHPOV9O6qmhgN7Wg7ar4nqQqboi1sEHu-AoXpPb84Ml5E-e_se8idt-B_lR01yqiwd1mnMDbCPrniKS5nb4bu1cpRFsu5NiGfgFY2ewanbStaftfIdqNg2PbwNNQgeVyYh63wJQJn5rOurCP9Xk4fw';

            $gcm = new GCM();
            $registatoin_ids = array($regId);

            $responseInJson = array();
            $responseInJson["transactionId"] = $transactionId;
            $responseInJson["isComplete"] = $userAction;

            $message = array("message" => json_encode($responseInJson));

            $result = $gcm->send_notification($registatoin_ids, $message);
            $responseInJson = json_decode($result, true);

            if ($responseInJson[success] == 1) {
                $response["isSuccess"] = 1;
                $response["message"] = "Request Updated to the Donor";
                echo json_encode($response);
            } else {
                $response["isSuccess"] = 0;
                $response["message"] = "Request Not Updated";
                echo json_encode($response);
            }


        } else {
            $response["isSuccess"] = 0;
            $response["message"] = "Request Not Sent.Donor Not found";
            echo json_encode($response);
        }


    } else {
        $response["isSuccess"] = 0;
        $response["message"] = "Error in updating.Request ID not found";
        echo json_encode($response);
    }


} else {
    $response["isSuccess"] = 0;
    $response["message"] = "Required field(s) is missing";
    echo json_encode($response);
}



?>	