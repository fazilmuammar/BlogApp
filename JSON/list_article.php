<?php 

	require_once('connection.php');

	$title 			= $_REQUEST['title'];
	$category_id 	= $_REQUEST['category_id'];

	$query = mysqli_query($conn, "SELECT con.*, cat.name AS category FROM content AS con JOIN category AS cat ON con.category_id = cat.category_id WHERE con.title like '%".$title."%' AND con.status='confirm' ORDER BY con.content_id DESC");
	
	if (!empty($category_id)) {
		$query = mysqli_query($conn, "SELECT con.*, cat.name AS category FROM content AS con JOIN category AS cat ON con.category_id = cat.category_id WHERE con.title like '%".$title."%' AND cat.category_id='".$category_id."' AND con.status='confirm' ORDER BY con.content_id DESC");
	}

	$result = array();
	while($row = mysqli_fetch_array($query)){
		$created = date("d/m/y h:i A", strtotime($row['created']));
		$updated = date("d/m/y h:i A", strtotime($row['updated']));

		$img = $row['image'];
		if (!file_exists("../BlogApp/images/".$img)) {
			$img = "";
		}

		array_push($result,array(
			'content_id'	=> $row['content_id'],
			'category_id'	=> $row['category_id'],
			'category'		=> strtoupper($row['category']),
			'author'		=> $row['author'],
			'title'			=> $row['title'],
			'image'			=> $img,
			'created'		=> $created,
			'updated'		=> $updated,
			'status'		=> $row['status'],
			'view'			=> $row['view'],
			'love'			=> $row['love'],
		));
	}
	
	echo json_encode(array('result'=>$result));
	mysqli_close($conn);

 ?>