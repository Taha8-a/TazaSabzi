<?php
	include 'config/db_config.php';

	$data = file_get_contents("php://input");

	$request = json_decode($data);

	$response = array();

	$isValidRequest = false;

	//{"action":"REGISTER_USER","userName":"Mr. Ahmed"}

	//REGISTER_USER
	//UPDATE_USER
	//ADD_SABZI_PRODUCTS
	//GET_SABZI_PRODUCTS
	//UPDATE_SABZI_PRODUCTS
	//DELETE_SABZI_PRODUCTS

	if(isset($request->{'action'})){
		if($request->{'action'} == 'REGISTER_USER'){
			$isValidRequest = true;	

			$userName = $request -> {'userName'};
			$userEmail = $request -> {'userEmail'};
			$userPassword = $request -> {'userPassword'};
			$query = "INSERT INTO user(`name`,`email`,`password`) values('".$userName."','".$userEmail."','".$userPassword."')";
			$result = mysqli_query($connection,$query);
			if($result){
				$response['userId'] = mysqli_insert_id($connection);
				$response['status'] = true; 
				$response['responseCode'] = 0; //success
				$response['message'] ="User registered successfully";
			}
			else{
				$response['status'] = false; 
				$response['responseCode'] = 102; //User registration failed
				$response['message'] ="User registration failed";
			}
		}

		if($request->{'action'} == 'UPDATE_USER'){
			$isValidRequest = true;

			$userId = $request->{'userId'};
			$userName = $request -> {'userName'};
			$userEmail = $request -> {'userEmail'};
			$userPassword = $request -> {'userPassword'};
			$query = "UPDATE user SET name='".$userName."', email='".$userEmail."', password='".$userPassword."' WHERE id = '".$userId."'";
			$result = mysqli_query($connection,$query);
			if($result){
				$response['status'] = true; 
				$response['responseCode'] = 0; //success
				$response['message'] ="User updated successfully";
			}
			else{
				$response['status'] = false; 
				$response['responseCode'] = 103; //User modification failed
				$response['message'] ="User modification failed";
			}
		}

		if($request->{'action'} == 'ADD_SABZI_PRODUCTS'){
			$isValidRequest = true;	

			$userId = $request -> {'userId'};
			$name = $request -> {'name'};
			$price = $request -> {'price'};

			$query = "INSERT INTO product(`name`,`price`,`user_id`) values('".$name."','".$price."','".$userId."')";
			$result = mysqli_query($connection,$query);
			if($result){
				$response['productId'] = mysqli_insert_id($connection);
				$response['status'] = true; 
				$response['responseCode'] = 0; //success
				$response['message'] ="Product inserted successfully";
			}
			else{
				$response['status'] = false; 
				$response['responseCode'] = 104; //Product insertion failed
				$response['message'] ="Product insertion failed";
			}

		}

		if($request->{'action'} == 'GET_SABZI_PRODUCTS'){
			$isValidRequest = true;	
            $userId = $request -> {'userId'};

			$query = "SELECT p.id as productId, u.id as userId,p.*,u.* FROM product p INNER JOIN user u on p.user_id = u.id";
			$result = mysqli_query($connection,$query);
			if($result && mysqli_num_rows($result)>0){
				$myProducts = array();
				$allProducts = array();
				while(($row = mysqli_fetch_assoc($result))!=null){
					$product = array();
					$product["productId"] = $row['productId'];
					$product["name"] = $row['name'];
					$product["price"] = $row['price'];

					$allProducts[] = $product;

					if($row['userId'] == $userId){
						$myProducts[] =  $product;
					}
				}

				$response['status'] = true; 
				$response['responseCode'] = 0; //Products are available
				$response['message'] ="Products are available";
				$response['allProducts'] =  $allProducts;
				$response['allProducts']  = $myProducts;
			}
			else{
				$response['status'] = false; 
				$response['responseCode'] = 105; //Products are not available
				$response['message'] ="Products are not available";
			}
		}
		
		if($request->{'action'} == 'UPDATE_SABZI_PRODUCTS'){
			$isValidRequest = true;	

			$userId = $request -> {'userId'};
			$productId = $request -> {'productId'};
			$name = $request -> {'name'};
			$price = $request -> {'price'};

			$query = "UPDATE product SET name='".$name."',price='".$price."' WHERE id='".$productId."'";
			$result = mysqli_query($connection,$query);
			if($result){
				$response['productId'] = $productId;
				$response['status'] = true; 
				$response['responseCode'] = 0; //success
				$response['message'] ="Product updated successfully";
			}
			else{
				$response['status'] = false; 
				$response['responseCode'] = 106; //Blog updation failed
				$response['message'] ="Product updation failed";
			}

		}

		if($request->{'action'} == 'DELETE_SABZI_PRODUCTS'){
			$isValidRequest = true;	

			$userId = $request -> {'userId'};
			$productId = $request -> {'productId'};

			$query = "DELETE FROM product WHERE id='".$productId."'";

			$result = mysqli_query($connection,$query);
			if($result){
				$response['productId'] = $productId;
				$response['status'] = true; 
				$response['responseCode'] = 0; //success
				$response['message'] ="Product deleted successfully";
			}
			else{
				$response['status'] = false; 
				$response['responseCode'] = 107; //Blog deletion failed
				$response['message'] ="Product deletion failed";
			}
		}

		if(!$isValidRequest){
			$response['status'] = false; 
			$response['responseCode'] = 101; //Invalid request action
			$response['message'] ="Invalid request action";
		}

	}
	else{
		$response['status'] = false; 
		$response['responseCode'] = 100; //Request action not defined
		$response['message'] ="Request action not defined";
	}

	echo json_encode($response);

?>