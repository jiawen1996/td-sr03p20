<?php
  require_once('myModel.php');
  
  session_start();
  
  // URL de redirection par défaut (si pas d'action ou action non reconnue)
  $url_redirect = "index.php";
  
  if (isset($_REQUEST['action'])) {
  
      if ($_REQUEST['action'] == 'authenticate') {
          /* ======== AUTHENT ======== */
          if (!isset($_REQUEST['login']) || !isset($_REQUEST['mdp']) || $_REQUEST['login'] == "" || $_REQUEST['mdp'] == "") {
              // manque login ou mot de passe
              $url_redirect = "vw_login.php?nullvalue";
              
          } else {
             // --------- CHECK ENTERED VALUE BY USER --------------//
             $msgValidLogin = validField($_REQUEST['login']);
             $msgValidPwd = validField($_REQUEST['mdp']);
             if ($msgValidLogin == "ok" && $msgValidPwd == "ok") {

                $existLogin = findUser($_REQUEST['login']); 
                
                if ($existLogin) {
                    $utilisateur = findUserByLoginPwd($_REQUEST['login'], $_REQUEST['mdp']);
              
                    if ($utilisateur == false) {
                    // echec authentification
                    $url_redirect = "vw_login.php?badvalue";
                
                    } else {
                        // authentification réussie
                        $_SESSION["connected_user"] = $utilisateur;
                        $_SESSION["listeUsers"] = findAllUsers();
                        $url_redirect = "vw_moncompte.php";
                    }
                } else {
                    // utilisateur inexiste
                    $url_redirect = "vw_login.php?inexistelogin";
                }
             } else {
                 // champs non valid
                $url_redirect = "vw_login.php?forbiddenvalue=".$msgValid;
             }

              
          }
          
      } else if ($_REQUEST['action'] == 'disconnect') {
          /* ======== DISCONNECT ======== */
          unset($_SESSION["connected_user"]);
          $url_redirect = $_REQUEST['loginPage'] ;
          
      } else if ($_REQUEST['action'] == 'transfert') {
          /* ======== TRANSFERT ======== */
          if (is_numeric ($_REQUEST['montant'])) {
              if ( validField($_REQUEST['montant']) == "ok" && $_REQUEST['montant'].pos("-") == false ) {
                transfert($_REQUEST['destination'],$_SESSION["connected_user"]["numero_compte"], $_REQUEST['montant']);
                $_SESSION["connected_user"]["solde_compte"] = $_SESSION["connected_user"]["solde_compte"] -  $_REQUEST['montant'];
                $url_redirect = "vw_moncompte.php?trf_ok";
              } else {
                $url_redirect = "vw_moncompte.php?bad_mt=".$_REQUEST['montant'];
            }
              
              
          } else {
              $url_redirect = "vw_moncompte.php?bad_mt=".$_REQUEST['montant'];
          }
       
      } else if ($_REQUEST['action'] == 'sendmsg') {
          /* ======== MESSAGE ======== */
          addMessage($_REQUEST['to'],$_SESSION["connected_user"]["id_user"],$_REQUEST['sujet'],$_REQUEST['corps']);
          $url_redirect = "vw_moncompte.php?msg_ok";
              
      } else if ($_REQUEST['action'] == 'msglist') {
          /* ======== MESSAGE ======== */
          $_SESSION['messagesRecus'] = findMessagesInbox($_REQUEST["userid"]);
          $url_redirect = "vw_messagerie.php";
              
      } 

       
  }  
  

  function validField($field) {
        if (strpos($field,"/") == true || strpos($field,"\'" == true) || strpos($field,"\"") == true || strpos($field,":") == true) {
            return "Login contient des caractères interdits / ' \" : ";
        } else {
            return "ok";
        }
    }

  header("Location: $url_redirect");

?>
