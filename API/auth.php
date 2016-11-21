<?php

include("./connection.php");
include("./functions.php");

$params = $_REQUEST;
$error = true;
$message = "Unknow error";
$response = array();

$params = splitData($params);

switch($params['action']){
	case "signup":
		//do signup
		$query = "INSERT INTO `matheusn_reclameonibus`.`user` (
					`Data` ,
					`nome` ,
					`telefone`,
					`email` ,
					`senha` ,
					`bairro`
					)
				  VALUES (";
       	$query.="NOW(), '".$params['nome']."' , '".$params['telefone']."' , '".$params['email']."' , '".$params['senha']."' , '".$params['bairro']."')";

		//echo $query;

		$result = mysql_query ($query);

		if(!$result){
			switch (mysql_errno($conn)) {
				case '1022':
				case '1062':
					$message = "Usuário já cadastrado.";
					break;
				
				default:
					$message = "Infelizmente não foi possível realizar o cadastro. Erro ".mysql_errno($conn);
					break;
			}
			$response = null;
		}
		else{
			$error = false;
			$message = "Cadastro criado com sucesso!";
			$response = mysql_insert_id();
		}

		break;
	case "login":
		//do login
		$query = "SELECT * FROM user WHERE email='".$params['email']."'";

		$result = mysql_query ($query);

		if(!$result){
			$message = "Não foi possível realizar o login. Erro ".mysql_errno($conn);
			$response = null;
		}
		else{
			if(mysql_num_rows($result) > 0){
				while ($row = mysql_fetch_assoc($result)) {
					if($row['senha'] === $params['senha']){
						$error = false;
						$message = "Login efetuado com sucesso!";
						$response = $row;
					}
					else{
						$message = "Email ou senha inválidos";
						$response = null;
					}
				}
			}
			else{
				$message = "Email ou senha inválidos";
				$response = null;
			}
		}
		break;
	case "pass":
		//send email
		$query = "SELECT * FROM user WHERE email='".$params['email']."'";
		$result = mysql_query ($query, $conn);
		
		if(!$result){
			$message = mysql_error();
			$response = null;
		}
		else{
			if(mysql_num_rows($result) > 0){
				$user = mysql_fetch_row($result);
				$emailResponse = sendEmail($user[5], $params['email']);
				
				$error = false;
				$message = "A senha foi enviada para o seu email.";
				$response = null;
			}
			else{
				$message = "Não foi encontrado nenhum cadastro com este email.";
				$response = null;
			}
			
		}

		break;
	case "edit":
		//save new user data
		$query = "UPDATE user SET 
                    nome = '".$params['nome']."',
                    telefone = '".$params['telefone']."',
                    bairro = '".$params['bairro']."'
                    
                    WHERE user.email='".$params['email']."'";

		//echo $query;

		$result = mysql_query ($query, $conn);

		if(!$result){
			$message = mysql_error();
			$response = null;
		}
		else{
			$error = false;
			$message = "Alterações salvas com sucesso!";
			$response = null;
		}

		break;
	case "change_pass":
		$query = "UPDATE user SET senha='".$params['novaSenha']."'
					WHERE user.email='".$params['email']."'
					AND user.senha='".$params['senha']."'";
		$result = mysql_query ($query, $conn);
		if(mysql_affected_rows()==0){
			$message = "Senha original inválida.";
			$response = null;
		}
		else{
			$error = false;
			$message = "Senha alterada com sucesso!";
			$response = null;
		}
		break;
	default:
		$message = "Nenhuma ação foi definida";
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