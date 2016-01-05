<?php

$response = array();
if (isset($_GET['userId']) && isset($_GET['lattitude']) && isset($_GET['longitude'])) {

    $userId = $_GET['userId'];
    $lattitude = $_GET['lattitude'];
    $longitude = $_GET['longitude'];
    $latlong=$lattitude.';'.$longitude;
    $date=date("Y-m-d h:i:sa");
    echo $date."<br/>";
    $query = "Update donar_details SET last_known_location='$latlong' ,last_known_location_time	='$date'  where user_id='$userId'";
    
    echo $query;

    require_once __dir__ . '/db_config.php';
    $con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE) or die("Error Connecting Database " .
        mysqli_connect_error($con));

    $result = mysqli_query($con, $query);


    if (mysqli_affected_rows($con) > 0) {

        $response["isLocationUpdated"] = 1;
        $response["message"] = "Location Updated Successfuly";
        echo json_encode($response);

    }else{
        $response["isLocationUpdated"] = 0;
        $response["message"] = "Location Not Updated";
        echo json_encode($response);
    }


} else {
    $response["isLocationUpdated"] = 0;
    $response["message"] = "Required field(s) is missing";
    echo json_encode($response);
}



?>	