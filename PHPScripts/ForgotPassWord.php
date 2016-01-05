<?php
	
	
	$response = array();
	if (isset($_GET['email']))
	{
		
		$username = $_GET['email'];
		$tempPassWord=randomPasswordGenerator();
		$query = "Update User_Credntials Set Temp_Password='$tempPassWord',do_change_password=1 where Email='$username'";
		require_once __DIR__ . '/db_config.php';
		$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error Connecting Database " . mysqli_connect_error($con)); 
		
		$result = mysqli_query($con,$query);
		
		
		if (!empty($result)) {

			if (mysqli_affected_rows ( $con ) > 0) {
		
		            	
				$to = $username;
				$subject = "Password Reset";
				$message = "Your password has been reset! Please find below password to continue using Vitality and keep donating the blood.\n\n 
				PassWord :".$tempPassWord."
				Please Change your password next login!
				\n\n\n This is a automated message. Replying to this email will not answered!";
				$header = "From: no-reply@blooddonor.com \r\n";
				$retval = mail ($to,$subject,$message,$header);
				
				if($retval){
					
					$response["isEmailSent"] = 1;
					$response["message"] = "Your password has been reset! Please check your email and change your password";
					echo json_encode($response);
				}
				else
				{
					
					$response["isEmailSent"] = 0;
					$response["message"] = "Error Resetting password! Contact Tech Support!";
					echo json_encode($response);
				}
			} 
			else{
				$response["isEmailSent"] = 0;
				$response["message"] = "Error Resetting password! Email Not Registered!";
				echo json_encode($response);
			}
		}
		else{
			$response["isEmailSent"] = 0;
			$response["message"] = "Error Resetting password! Contact Tech Support!";
			echo json_encode($response);
		}
		
		
	}
	else {
		$response["isEmailSent"] = 0;
		$response["message"] = "Required field(s) missing";
		echo json_encode($response);
	}
	
	
	function randomPasswordGenerator( ) {
		$chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		return substr(str_shuffle($chars),0,10);
	}
	
	
?>	