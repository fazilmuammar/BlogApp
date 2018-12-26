<?php 

	require_once('connection.php');
	 

	$str_query = "SELECT cat.*, COUNT(con.content_id) AS content_id FROM category AS cat LEFT JOIN content AS con ON cat.category_id = con.category_id GROUP BY cat.category_id ORDER BY cat.name DESC";
	$query = mysqli_query($conn, $str_query);
	
	$result = array();
	while($row = mysqli_fetch_array($query)){
		array_push($result,array(
			'category_id'	=> $row['category_id'],
			'name'			=> $row['name'],
			'image'			=> $row['image'],
			'content_id'	=> $row['content_id']
		));
	}
	
	echo json_encode(array('result'=>$result));
	mysqli_close($conn);

 ?>