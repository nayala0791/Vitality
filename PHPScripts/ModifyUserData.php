<?php
        
	$response = array();
	if (isset($_POST['uid']))
	{
		
		
		$uid= $_POST['uid'];
		
		$name=$_POST['name'];
		$email=$_POST['email'];
		$phoneNumber=$_POST['phoneNumber'];
		$address=$_POST['address'];
		
		$gothra=$_POST['gothra'];
		$rashi=$_POST['rashi'];
		$nakshatra=$_POST['nakshatra'];
		
		
		
		$updateUserMasterQuery= "Update userMaster set name='$name',email='$email',phoneNumber='$phoneNumber',address='$address' where userId='$uid'";
		$updateJapaDetailsQuery="Update japaDetails set gothra='$gothra',rashi='$rashi',nakshatra='$nakshatra' where userid='$uid'";
		
		require_once __DIR__ . '/db_config.php';
		$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error Connecting Database " . mysqli_connect_error($con)); 
		
		$isUserMasterUpdated=false;
		$isJapaDetailUpdated=false;
		
		
		
		$resultUserMasterUpdate = mysqli_query($con,$updateUserMasterQuery);
	
	         if (mysqli_affected_rows($con)  > 0) {
				$isUserMasterUpdated=true;
		      } 
	
		
		$resultJapaDetailUpdate = mysqli_query($con,$updateJapaDetailsQuery);
        	
		
			
		
		
		
			if (mysqli_affected_rows($con)> 0) {
				$isJapaDetailUpdated=true;
		      } 
			
		
		
		
		
		if($isUserMasterUpdated && $isJapaDetailUpdated)
		{
		
		$response["isProfileUpdated"] = 1;
		$response["message"] = "Profile Updated Successfully";
		echo json_encode($response);
		
		}
		else
		{
		
		$response["isProfileUpdated"] = 0;
		$response["message"] = "Oops!Something went wrong.Please try again";
		echo json_encode($response);
		}
		
		
	}
	else {
		$response["isProfileUpdated"] = 0;
		$response["message"] = "Required field(s) is missing";
		echo json_encode($response);
	}
	
	
	
	
?>	