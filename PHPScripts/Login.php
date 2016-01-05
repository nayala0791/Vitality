<?php
	$response = array();
	if (isset($_GET['uname']) && isset($_GET['pword']))
	{
		
		$username = $_GET['uname'];
		$password = $_GET['pword'];
		$query = "SELECT * From User_Credntials m   WHERE m.email='$username' and m.Password='$password' and m.do_change_password=0 and m.is_Active=1";
		
		require_once __DIR__ . '/db_config.php';
		$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error Connecting Database " . mysqli_connect_error($con)); 
		$result = mysqli_query($con,$query);
		
		$queryForTemp = "SELECT * FROM User_Credntials m WHERE m.email='$username' and m.Temp_Password='$password' and m.do_change_password=1 and m.is_Active=1";
		$resultForTempPassword = mysqli_query($con,$queryForTemp);
		
		
		
		
			if (mysqli_num_rows($result) > 0) {
				$result = mysqli_fetch_array($result);
				
				if($result["Type"]==0) // Signifies that the user is a Donor
				{
					$response["isLoggedIn"] = 1;
					$response["message"] = "Logged in Successfully";
					$response["changePassWord"] = 0;	
					$response["uid"]=(int)$result["Id"];
					$response["userType"]=0;
					
					
					
					// Retrieve the Donor profile
					$donorProfileQuery="Select * from donar_details donor where donor.user_id='$result[Id]'";
					
					$donorProfileResult=mysqli_query($con,$donorProfileQuery);
					
					if(!empty($donorProfileResult) && mysqli_num_rows($donorProfileResult)>0)
					{
						$donorProfileRow=mysqli_fetch_array($donorProfileResult);
						if($donorProfileRow!=null)
						{
							$userDetail = array();
							$userDetail["userId"] = (int)$result["Id"];
							$userDetail["bloodGroup"]=$donorProfileRow["blood_group"];
							$userDetail["gender"]=$donorProfileRow["gender"];
							$userDetail["email"]=$result["Email"];
							$userDetail["numberOfDonation"]=$donorProfileRow["number_of_donation"];
							$userDetail["readyToDonate"]=$donorProfileRow["ready_to_donate"];
							$userDetail["phoneNumber"]=$donorProfileRow["phonenumber"];
							$userDetail["altPhoneNumber"]=$donorProfileRow["alt_phone_number"];
				                        $userDetail["address"]=$donorProfileRow["address"];
						        $userDetail["lastKnownLocation"]=$donorProfileRow["last_known_location"];
						        $userDetail["lastKnownTime"]=$donorProfileRow["last_known_location_time"];
						        
						
							$response["donorResponse"] = array();
							array_push($response["donorResponse"], $userDetail);	
						}
					}
					
					
					
					echo json_encode($response);
					
					
				}
				
				else // User is Hospital 
				{
					$response["isLoggedIn"] = 1;
					$response["message"] = "Logged in Successfully";
					$response["changePassWord"] = 0;	
					$response["uid"]=(int)$result["Id"];
					$response["userType"]=1;
					$userDetail = array();
					
					// Retrieve the Hospital Details
					
					$hospitalDetailQuery="Select * from hospital where user_id='$result[Id]'";
					
					$hospitalDetailResult=mysqli_query($con,$hospitalDetailQuery);
					
					if(!empty($hospitalDetailResult) && mysqli_num_rows($hospitalDetailResult)>0)
					{
						$hospitalDetailRow=mysqli_fetch_array($hospitalDetailResult);
						if($hospitalDetailRow!=null)
						{
							$userDetail = array();
							$userDetail["userId"] = (int)$result["Id"];
							$userDetail["hospitalName"]=$hospitalDetailRow["hospital_name"];
							$userDetail["hospitalAddress"]=$hospitalDetailRow["hospital_address"];
							$userDetail["email"]=$result["Email"];
							$userDetail["speciality"]=$hospitalDetailRow["speciality"];
							$userDetail["phoneNumber"]=$hospitalDetailRow["phone_number"];
							$userDetail["hospitalLicense"]=$hospitalDetailRow["hospital_license"];
							$userDetail["alternativePhonenumber"]=$hospitalDetailRow["alternative_phonenumber"];
					
					$response["hospitalResponse"] = array();
					array_push($response["hospitalResponse"], $userDetail);		
						}
					}
					
					
					echo json_encode($response);
				}
			}
		
		else if(!empty($resultForTempPassword)) {
		

				
				
					if (mysqli_num_rows($resultForTempPassword) > 0) {
					
                        $resultForTempPassword = mysqli_fetch_array($resultForTempPassword);
                        
                        
            	        if($resultForTempPassword["Type"]==0) // Signifies that the user is a Donor
            	        {
							$response["isLoggedIn"] = 1;
							$response["message"] = "Logged in Successfully";
							$response["changePassWord"] = 1;	
							$response["uid"]=(int)$resultForTempPassword["Id"];
							$response["userType"]=0;
							
							
							
							// Retrieve the Donor profile
							$donorProfileQuery="Select * from donar_details donor where donor.user_id='$resultForTempPassword[Id]'";
							
							$donorProfileResult=mysqli_query($con,$donorProfileQuery);
							
							if(!empty($donorProfileResult) && mysqli_num_rows($donorProfileResult)>0)
							{
							
								$donorProfileRow=mysqli_fetch_array($donorProfileResult);
								if($donorProfileRow!=null)
								{
									$userDetail = array();
									$userDetail["userId"] = (int)$resultForTempPassword["Id"];
									$userDetail["bloodGroup"]=$donorProfileRow["blood_group"];
									$userDetail["gender"]=$donorProfileRow["gender"];
									$userDetail["email"]=$resultForTempPassword["Email"];
									$userDetail["numberOfDonation"]=$donorProfileRow["number_of_donation"];
									$userDetail["readyToDonate"]=$donorProfileRow["ready_to_donate"];
									$userDetail["phoneNumber"]=$donorProfileRow["phonenumber"];
									$userDetail["address"]=$donorProfileRow["address"];
									$userDetail["altPhoneNumber"]=$donorProfileRow["alt_phone_number"];
				                                        $userDetail["address"]=$donorProfileRow["address"];
						                        $userDetail["lastKnownLocation"]=$donorProfileRow["last_known_location"];
						                        $userDetail["lastKnownTime"]=$donorProfileRow["last_known_location_time"];
						        
								$response["donorResponse"] = array();
								array_push($response["donorResponse"], $userDetail);
									
								}
							}
							
							
							
							echo json_encode($response);
							
							
						}
            	        
            	        else // User is Hospital 
            	        {
            	        				
							$response["isLoggedIn"] = 1;
							$response["message"] = "Logged in Successfully";
							$response["changePassWord"] = 1;	
							$response["uid"]=(int)$resultForTempPassword["Id"];
							$response["userType"]=1;
							$userDetail = array();
							
							// Retrieve the Hospital Details
							
							$hospitalDetailQuery="Select * from hospital where user_id='$resultForTempPassword[Id]'";
							
							$hospitalDetailResult=mysqli_query($con,$hospitalDetailQuery);
							
							if(!empty($hospitalDetailResult) && mysqli_num_rows($hospitalDetailResult)>0)
							{
								$hospitalDetailRow=mysqli_fetch_array($hospitalDetailResult);
								if($hospitalDetailRow!=null)
								{
									$userDetail = array();
									$userDetail["userId"] = (int)$resultForTempPassword["Id"];
									$userDetail["hospitalName"]=$hospitalDetailRow["hospital_name"];
									$userDetail["hospitalAddress"]=$hospitalDetailRow["hospital_address"];
									$userDetail["email"]=$resultForTempPassword["Email"];
									$userDetail["speciality"]=$hospitalDetailRow["speciality"];
									$userDetail["phonenumber"]=$hospitalDetailRow["phone_number"];
									$userDetail["hospitalLicense"]=$hospitalDetailRow["hospital_license"];
									$userDetail["alternativePhonenumber"]=$hospitalDetailRow["alternative_phonenumber"];
							
							
							$response["hospitalResponse"] = array();
							array_push($response["hospitalResponse"], $userDetail);
								}
							}
							
							
							echo json_encode($response);
						}
						} else {
						$response["isLoggedIn"] = 0;
						$response["changePassWord"] = 0;
						$response["message"] = "Please check user name password combination";
						echo json_encode($response);
					}
				}		
				
			else {
			$response["isLoggedIn"] = 0;
			$response["changePassWord"] = 0;
            $response["message"] = "You dont have an account";
            echo json_encode($response);
		}
		} else {
		$response["isLoggedIn"] = 0;
		$response["changePassWord"] = 0;
		$response["message"] = "Required field(s) is missing";
		echo json_encode($response);
	}
	
	
?>