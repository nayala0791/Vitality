<?php
	
	
	$response = array();
	if (isset($_GET['uid']))
	{
		
		$uid= $_GET['uid'];
		
		
		$deleteUserQuery= "Update userMaster set isActive=0 where userId='$uid'";
		
		require_once __DIR__ . '/db_config.php';
		$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error Connecting Database " . mysqli_connect_error($con)); 
		
			
		if (mysqli_query($con, $deleteUserQuery)) {
                     $response["isProfileDeleted"] = 1;
		$response["message"] = "Profile Deleted Successfully!";
		echo json_encode($response);
                } else {
                $response["isProfileDeleted"] = 0;
		$response["message"] = "Oops! Something went wrong.Please try again!";
		echo json_encode($response);
                }
		

		
		
		
		
		
		
	}
	else {
		$response["isProfileDeleted"] = 0;
		$response["message"] = "Required field(s) is missing";
		echo json_encode($response);
	}
	
	
	
	
?>	