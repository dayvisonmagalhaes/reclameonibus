<?php

function sendEmail($password, $email){
	//1 – Definimos Para quem vai ser enviado o email
	$para = $email;
	// 3 - resgatar o assunto digitado no formulário e  grava na variavel //$assunto
	$assunto = "Senha Reclame Ônibus";
	//4 – Agora definimos a  mensagem que vai ser enviado no e-mail
	$mensagem = "<html>
	                <body>
	                    <center>
	                    <font face='Helvetica'>
	                        <div>
	                        <div>
	                        <br>
	                        <br/>
	                        <label>Essa é sua senha para acessar o <b>Reclame Ônibus</b>.</label>
	                        Ao entrar no aplicativo você poderá alterar a sua senha.

	                        <div class='well text-center'>
	                        <h3>".$password."</h3>
	                        </h2>

	                        </div>
	                        <font color='gray'><label><i>Seus dados pessoais estão vinculadas a esta senha. Nunca compartilhe!</i></label>
	                        </div>
	                        <h6><i>Não responda este email.</i></h6></font>
	                    </font>
	                    </center>
	                </body>
	            </html>";
	//5 – agora inserimos as codificações corretas e  tudo mais.
	$headers  = "MIME-Version: 1.0\r\n";
	$headers .=  "Content-Type:text/html; charset=UTF-8\r\n";
	$headers .= "From:  Ajuda Reclame Ônibus \r\n";

	mail($para, $assunto, $mensagem, $headers);
}


?>