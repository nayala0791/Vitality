<?php

$response = array();

$listAllActiveDonorQuery =
    "Select * from  User_Credntials users,donar_details donors where users.Id=donors.user_id and users.is_Active=1 and donors.ready_to_donate=1";
require_once __dir__ . '/db_config.php';
$con = mysqli_connect(DB_SERVER, DB_USER, DB_PASSWORD, DB_DATABASE) or die("Error Connecting Database " .
    mysqli_connect_error($con));

$result = mysqli_query($con, $listAllActiveDonorQuery);

$response["donors"]=array();
if (mysqli_num_rows($result ) > 0) {

    while ($details = mysqli_fetch_array($result)) {
        $donors=array();
        $donors["username"]=$details["User_Name"];  
        $donors["email"]=$details["Email"];  
        $donors["uid"]=$details["user_id"];
        $donors["bloodGroup"]=$details["blood_group"];
        $donors["gender"]=$details["gender"];
        $donors["noOfDonation"]=$details["number_of_donation"];
        $donors["readyToDonate"]=$details["ready_to_donate"];
        $donors["phoneNumber"]=$details["phoneNumber"];
        $donors["altPhoneNumber"]=$details["alt_phone_number"];
        $donors["address"]=$details["address"];
        $donors["lastKnownLocation"]=$details["last_known_location"];
        $donors["lastKnownLocationTime"]=$details["last_known_location_time"];
        array_push($response["donors"],$donors);
        
    }

}
echo json_encode($response);


?>	