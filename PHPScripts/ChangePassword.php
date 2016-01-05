<?php
	
	
	$response = array();
	if (isset($_GET['uid']) && isset($_GET['currentPwd']) && isset($_GET['newPwd']))
	{
		
		$uid= $_GET['uid'];
		$tempPassWord=$_GET['currentPwd'];
		$passWord=$_GET['newPwd'];
		$query = "Select * From User_Credntials where Id='$uid' and Temp_Password='$tempPassWord'";
		require_once __DIR__ . '/db_config.php';
		$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error Connecting Database " . mysqli_connect_error($con)); 
		
		
		
		$result = mysqli_query($con,$query);
		
		if (!empty($result)) {
			if (mysqli_num_rows($result) > 0) {
				
				
				
				$changePassWordQuery="Update User_Credntials Set Password='$passWord',do_change_password=0 where Id='$uid'";
				
				
				$changePwdResult=mysqli_query($con,$changePassWordQuery);
				
				if(!empty($changePwdResult))
				{
					if($changePwdResult>0)
					{
						
						$response["isPwdChanged"] = 1;
						$response["message"] = "Password changed successfully!.Please login with new password!";
						echo json_encode($response);
						
					}
				}
				
				
			} 
			else{
				
				$response["isPwdChanged"] = 0;
				$response["message"] = "Error Changing password! Please check if the you entered current password correctly!";
				echo json_encode($response);
			}
		}
		else{
			$response["isPwdChanged"] = 0;
			$response["message"] = "Error Changing password! Please check if the you entered current password correctly!";
			echo json_encode($response);
		}
		
		
	}
	else {
		$response["isPwdChanged"] = 0;
		$response["message"] = "Required field(s) is missing";
		echo json_encode($response);
	}
	
	
	
	
?>	