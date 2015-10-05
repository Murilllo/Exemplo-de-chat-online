package amplastudio.sati;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedList;

/**
 * Created by Murillo on 05/10/2015.
 */
public class Conexao {

    /* String com o link para o banco de dados */
    static String urlPuxarDado = "http://www.sati.net78.net/load.php";
    static String urlEnviarDado = "http://www.sati.net78.net/put.php";







    /**
     * Puxa os dados do banco de dados do link passado.
     * @return Vetor de String contendo toda a conversa
     */
    public static String[] puxarConversa(){

        HttpURLConnection conexao = null; /* classe que permite conectar com o link */
        /* vetor dinâmico, permitindo retirar toda a conversa do banco de dados */
        LinkedList<String> informacoes = new LinkedList<>();

        try{ /* estre bloco try ele tenta executar as linhas a seguir, se algum erro ocorrer, o que está dentro do bloco catch será executado*/

            //Conecta ao servidor
            URL url = new URL(urlPuxarDado); /* transforma a string do link em um URL para criar a conexão */
            conexao = (HttpURLConnection) url.openConnection(); /* abre conexão com o link */

            //Receber dados do servidor
            InputStream in = conexao.getInputStream(); /* recebe o texto que está no link. Abra o link na internet para ver o que está sendo puxado */
            BufferedReader br = new BufferedReader(new InputStreamReader(in)); /* quebra o texto em várias linhas */

            String line;

            while( ( line = br.readLine() ) != null ) /* Adiciona na lista enquanto tivermos linhas para ler */
                informacoes.add(line);

        } catch(Exception e) {
            e.printStackTrace();/* printa a mensagem de erro, se ocorrer */
        } finally {
            if(conexao != null)
                conexao.disconnect(); /* se não houver falhas, desconecta */
        }


        return informacoes.toArray(new String[informacoes.size()]); /* transforma a LinkedList em um vetor de Strings */
    }







    /**
     * Envia um texto preenchido no EditText da janela principal
     * @param mensagem Dado para ser enviado
     */
    public static void enviarConversa(String mensagem){

        HttpURLConnection conexao = null; /* variável que nos permite conectar ao link do site */

        try{
            URL url = new URL(urlEnviarDado);
            conexao = (HttpURLConnection) url.openConnection(); /* abre uma conexão com o url passado */
            conexao.setRequestMethod("POST"); /* diz que queremos colocar os dados sem que eles apareçam no link, é desta forma que o servidor foi configurado para receber dados */
            conexao.setDoOutput(true); /* diz que queremos escrever dados no url passado */

            /* Como sabemos o tamanho da mensagem, coloca um tamanho fixo para o tamanho dos dados que serão enviados
             * Esta são recomendações da própria biblioteca, para melhoria na velocidade da conexão
             */
            conexao.setFixedLengthStreamingMode(mensagem.length());

            OutputStream os = conexao.getOutputStream(); /* pega o endereço do servidor onde é possível enviar os dados que queremos */
            os.write(mensagem.getBytes()); /* insere os dados no script do servidor para que possa ser lido e salvo no banco de dados. Os dados devem ser enviados como um vetor de bytes, por isso o getByte(), transforma o texto em bytes */
            os.close(); /* fecha a conexão, dizendo que todos os dados foram enviados */

            conexao.getResponseMessage(); /* pede para o servidor executar a página que salva os dados enviados no banco de dados */

        }catch(Exception e){
            e.printStackTrace(); /* se houver errors, escreve a mensagem de erro no terminal */

        } finally{

            if(conexao != null)
                conexao.disconnect(); /* se não houver falhas, desconecta do url */

        }
    }

}
