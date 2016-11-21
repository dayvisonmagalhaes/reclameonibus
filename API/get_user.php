<?php

include("./connection.php");
include("./functions.php");

$params = $_REQUEST;
$error = true;
$message = "Unknow error";
$response = array();

switch($params['action']){
	case "getUser":
		$query = "SELECT * 
					FROM `matheusn_reclameonibus`.`user`";

		//echo $query;

		$result = mysql_query ($query);

		if(!$result){
			if(mysql_errno($conn)) {
				$message = "Erro ".mysql_errno($conn);
			}
			$response = null;
		}
		else{
			$error = false;
			if(mysql_num_rows($result)==0){
				$message = "Nenhum usuário foi encontrado.";
				$response = null;
			}
			else{
				$message = "Usuários encontrados com sucesso!";

			}
		}
		break;
}

echo json_encode(
	array(
		"error"=>$error,
		"message"=>$message,
		"response"=>$response
	)
);

?>