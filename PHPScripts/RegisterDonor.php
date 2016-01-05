<?php
	$response = array();

	if (isset($_POST['email']) && isset($_POST['name']) && isset($_POST['phoneNumber'])&& isset($_POST['altPhoneNumber'])&& isset($_POST['bloodGroup'])&& isset($_POST['gender'])&& isset($_POST['address']))
	{
		$email = $_POST['email'];
		$name =$_POST['name'];
		$phoneNumber= $_POST['phoneNumber'];
		$altPhoneNumber=$_POST['altPhoneNumber'];
		$bloodGroup=$_POST['bloodGroup'];
		$gender=$_POST['gender'];
		$address = $_POST['address'];
		
		require_once __DIR__ . '/db_config.php';
		
		$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error Connecting Database " . mysqli_connect_error($con)); 
		
		//Check If email exists
		
		$query = "SELECT * from User_Credntials WHERE Email='$email'";
		
		$result = mysqli_query($con,$query);
		
		
		if (!empty($result)) {
			
			if (mysqli_num_rows($result) > 0) {
				
				$response["isRegistered"] = 0;
				$response["message"] = "Email already registered!";
				echo json_encode($response);
				
			} 
			else{
				$tempPwd = randomPasswordGenerator();
				$insertQuery="INSERT INTO User_Credntials(`User_Name`, `Email`, `Temp_Password`, `Password`, `do_change_password`,`Type`) VALUES ('$name','$email','$tempPwd', 'NULL', b'1',b'0')";
				
				
				
				mysqli_query($con,$insertQuery) or die ("Error in Query".mysqli_error($con)) ;
				
				
				$userId=mysqli_insert_id($con);
				
				
				
				$date = date_create();
				
				date_timestamp_set($date, time());
				
				
				$insertQueryForDonor="INSERT INTO donar_details(`user_id`, `blood_group`, `gender`, `phonenumber`, `alt_phone_number`, `address`, `last_updated_on`,`last_updated_by`) 
						VALUES ('$userId','$bloodGroup','$gender','$phoneNumber','$altPhoneNumber','$address','".date_format($date, 'Y-m-d H:i:s')."','admin')";
				
				
				mysqli_query($con,$insertQueryForDonor) or die ("Error in Query".mysqli_error($con)) ;
				
				
				
				$to = $email;
				$subject = "Blood Donor Finder!";
				$message = "Thanks for registering! Below is a temperary password to login for the first time. 
				PassWord :".$tempPwd."
				Change password at initial login!
				\n\n\n This is a automated message. Replying to this email will not answered!";
				$header = "From:no-reply@blooddonor.com \r\n";
				$retval = mail ($to,$subject,$message,$header);
				
				
				if(mysqli_insert_id($con)>0 && $retval)
				{
					$response["isRegistered"] = 1;
					$response["message"] = "Registration Successful. Please Check you email for credentials!";
					
					echo json_encode($response);
				}else
				
				{
					$response["isRegistered"] = 0;
					$response["message"] = "Error while registering! Please contact System Admin!";
					
					echo json_encode($response);
					
				}
				
				
			}
			
			} else {
			$response["isRegistered"] = 0;
			$response["message"] = "Error while registering! Please contact System Admin!";
			echo json_encode($response);
    		
		}
		} else {
		$response["isRegistered"] = 0;
		$response["message"] = "Required field(s) is missing";
		echo json_encode($response);
	}
	
	
	
	function randomPasswordGenerator( ) {
		$chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		return substr(str_shuffle($chars),0,10);
		
	}
	
	
	
?>