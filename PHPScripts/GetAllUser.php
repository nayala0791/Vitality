<?php
$response = array();
if (isset($_POST['uname']))
{
$username = $_POST['uname'];
$query = "SELECT * FROM japaDetails a, userMaster m   WHERE m.email='$username' and m.userId=a.userid and m.isActive=1";
require_once __DIR__ . '/db_config.php';
$con = mysqli_connect(DB_SERVER,DB_USER,DB_PASSWORD,DB_DATABASE) or die("Error Connecting Database " . mysqli_connect_error($con)); 
$result = mysqli_query($con,$query);

if (!empty($result)) {
        if (mysqli_num_rows($result) > 0) {
            $result = mysqli_fetch_array($result);
            
            if($result["isAdmin"]==1)
			{
            $response["message"] = "Retrieved All user data";
			$response["isGetData"] = 1;	
			$response["uid"]=$result["userid"];
			$response["isAdmin"]=1;
			$userDetail = array();
			$userDetail["userId"] = $result["userId"];
			$userDetail["name"] = $result["name"];
			$userDetail["email"] = $result["email"];
			$userDetail["rashi"] = $result["rashi"];
			$userDetail["gothra"] = $result["gothra"];
			$userDetail["address"] = $result["address"];
			$userDetail["nakshatra"] = $result["nakshatra"];
			$userDetail["phoneNumber"] = $result["phoneNumber"];
            $userDetail["japacount"] = $result["japaCount"];
			$response["userDetail"] = array();
			array_push($response["userDetail"], $userDetail);
			
			$userListQuery="SELECT * FROM japaDetails a, userMaster m Where m.userId=a.userid and m.isActive=1";
			
			$userListResult=mysqli_query($con,$userListQuery) ;
			
			$response["japaUsers"]=array();
			while($row=mysqli_fetch_array($userListResult))
			{
			 $user=array();
			 $user["userId"] = $row["userId"];
			 $user["name"]=$row["name"];
			 $user["email"]=$row["email"];
			 $user["rashi"]=$row["rashi"];
			 $user["gothra"]=$row["gothra"];
			 $user["address"]=$row["address"];
			 $user["nakshatra"]=$row["nakshatra"];
			 $user["phoneNumber"]=$row["phoneNumber"];
			 $user["japacount"]=$row["japaCount"];
			 $user["isAdmin"]=$row["isAdmin"];
			 array_push($response["japaUsers"], $user);
			}
			
			echo json_encode($response);
			
			               	        
           }
           
		} 
    } else {
	     
		$response["isGetData"] = 0;
        $response["message"] = "Something went wrong!";
        echo json_encode($response);
    }
} else {
    $response["isGetData"] = 0;
	$response["message"] = "Required field(s) is missing";
    echo json_encode($response);
}


?>