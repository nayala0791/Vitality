<?php
	
	
	$response = array();
	if (isset($_POST['uid']) && isset($_POST['japaCount']))
	{
		
		$uid= $_POST['uid'];
		$japaCount=$_POST['japaCount'];
		
		$query = "Select japaCount from japaDetails where userid='$uid'";
		require_once __DIR__ . '/db_config.php';
		$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error Connecting Database " . mysqli_connect_error($con)); 
		
		$result = mysqli_query($con,$query);
		
		if (!empty($result)) {
			if (mysqli_num_rows($result) > 0) {
				
				
				$japaResultArray=mysqli_fetch_array($result);
				$updatedJapaCount=$japaResultArray["japaCount"]+ $japaCount;
				
				
				
				$updateJapaQuery="Update japaDetails Set japaCount='$updatedJapaCount' where userid='$uid'";
				
				$updateJapaResult=mysqli_query($con,$updateJapaQuery);
				
				if(!empty($updateJapaResult))
				{
					if($updateJapaResult>0)
					{

						$response["isCountUpdated"] = 1;
						$response["uid"]=$uid;
						$response["japaCount"]=$updatedJapaCount;
			                        $response["message"] = "JapaCount Updated Successfully!";
			                        echo json_encode($response);
						
					}
				}
				
				
			} 
			else{
				
			$response["isCountUpdated"] = 0;
			$response["message"] = "Error Updating japa Count! Please Try again!";
			echo json_encode($response);
			}
		}
		else{
			$response["isCountUpdated"] = 0;
			$response["message"] = "Error Updating japa count! Please Try again!";
			echo json_encode($response);
		}
		
		
	}
	else {
		$response["isCountUpdated"] = 0;
		$response["message"] = "Required field(s) is missing";
		echo json_encode($response);
	}
	
	
	
	
?>	