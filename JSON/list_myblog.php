<?php 

	require_once('connection.php');
	$user_id = $_REQUEST['user_id'];

	$query = mysqli_query($conn, "SELECT con.*, cat.name AS category FROM content AS con LEFT JOIN category AS cat ON con.category_id = cat.category_id WHERE con.user_id='$user_id' ORDER BY con.content_id DESC");
	
	$result = array();
	
	while($row = mysqli_fetch_array($query)){
		$created = date("d/m/y h:i A", strtotime($row['created']));
		$updated = date("d/m/y h:i A", strtotime($row['updated']));
		array_push($result,array(
			'content_id'	=> $row['content_id'],
			'category_id'	=> $row['category_id'],
			'category'		=> $row['category'],
			'author'		=> $row['author'],
			'title'			=> $row['title'],
			'image'			=> $row['image'],
			'created'		=> $created,
			'updated'		=> $updated,
			'content'		=> $row['content'],
			'status'		=> $row['status'],
			'view'			=> $row['view'],
			'love'			=> $row['love'],
		));
	}
	
	echo json_encode(array('result'=>$result));
	mysqli_close($conn);

 ?>