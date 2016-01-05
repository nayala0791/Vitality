<?php
	
	
	$response = array();
	if (isset($_GET['userId'])&& isset($_GET['regId'])&&isset($_GET['imei'])&&isset($_GET['deviceModel']))
	{
		
		$userid = $_GET['userId'];
		$regId=$_GET['regId'];
		$imei=$_GET['imei'];
		$deviceModel=$_GET['deviceModel'];
		
		
		$checkQuery = "Select * from device_info where userId='$userid' and deviceRegistrationId='$regId' and IMEI='$imei';";
		$insertQuery= "Insert into device_info(`userId`,`deviceRegistrationId`,`IMEI`,`device_model`) VALUES ('$userid','$regId','$imei','$deviceModel');";
		
		require_once __DIR__ . '/db_config.php';
		$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error Connecting Database " . mysqli_connect_error($con)); 
		$result = mysqli_query($con,$checkQuery);
		
					
		if (mysqli_num_rows($result)==0) {

		$result = mysqli_query($con,$insertQuery);
		
		$id=mysqli_insert_id($con);
		
		if($id > 0)
		{
			$response["isRegistered"] = 1;
			$response["message"] = "Device Info Stored Successfuly";
			echo json_encode($response);
		}
		else{
			$response["isRegistered"] = 0;
			$response["message"] = "Problem in device info storage";
			echo json_encode($response);
		}
					
		}
		else{
			$response["isRegistered"] = 0;
			$response["message"] = "Already registered!";
			echo json_encode($response);
		}
		
		
	}
	else {
		$response["isRegistered"] = 0;
		$response["message"] = "Required field(s) is missing";
		echo json_encode($response);
	}
	
	
?>	