<?php 

	require_once('connection.php');

	$title 			= $_REQUEST['title'];
	$category_id 	= $_REQUEST['category_id'];
	$page 			= $_REQUEST['page'];

	$sql 	= "SELECT con.*, cat.name AS category FROM content AS con JOIN category AS cat ON con.category_id = cat.category_id WHERE con.title like '%".$title."%' AND con.status='confirm' ORDER BY con.content_id DESC LIMIT $page,3 ";

	if (!empty($category_id)) {
		$sql = "SELECT con.*, cat.name AS category FROM content AS con JOIN category AS cat ON con.category_id = cat.category_id WHERE con.title like '%".$title."%' AND cat.category_id='".$category_id."' AND con.status='confirm' ORDER BY con.content_id DESC";
	}

	// echo $sql; die();
	$query 	= mysqli_query($conn, $sql);


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

	$sql1 = "SELECT COUNT(con.content_id) AS count_id FROM content AS con JOIN category AS cat ON con.category_id = cat.category_id WHERE con.title like '%".$title."%' AND con.status='confirm' ORDER BY con.content_id DESC";

	$query1 = mysqli_query($conn, $sql1);

	$content_id 	= mysqli_fetch_object($query1);
	$activity_id 	= $content_id->count_id;
	
	echo json_encode(array('content_count'=>$activity_id, 'result'=>$result));
	mysqli_close($conn);

 ?>