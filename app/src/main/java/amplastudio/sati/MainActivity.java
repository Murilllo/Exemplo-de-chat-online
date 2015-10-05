package amplastudio.sati;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;



public class MainActivity extends AppCompatActivity implements Button.OnClickListener {


    static String meuNome = "COLOQUE SEU NOME"; /* string para o nome do "perfil" */

    /* string que vai conter a conversa no banco de dados e o autor da mensagem */
    String[] conversa;
    String[] autor;


    ListaAdapter adapter;/* classe que gerencia os elementos que serão mostrados na lista */

    /* elementos da janela */
    Button botao;
    ListView lista;
    EditText editor;

    /* variável que nos permite executar algum código após um tempo escolhido, em milisegundos*/
    Handler handler;







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botao = (Button) this.findViewById(R.id.botao); /* findViewById é a função que busca o elemento na tela */
        lista = (ListView) this.findViewById(R.id.lista);
        editor = (EditText) this.findViewById(R.id.editor_de_texto);

        botao.setOnClickListener(this); /* ativa o método onClick para responder a eventos de click. O "this" é o Button.OnClickListener */

        handler = new Handler();
    }







    /* este bloco de código é executado quando a janela está visível para o usuário */
    @Override
    public void onResume(){
        super.onResume();

        handler.postDelayed(new Runnable() { /* atrasa a execução deste código em um determinado tempo */



            @Override
            public void run() {
                PuxarDados puxar = new PuxarDados();
                puxar.execute(); /* puxa os dados no plano de fundo  */
                handler.postDelayed(this, 2000); /* executa novamente este mesmo código (por isso a palavra this ali) com 2 segundos de atraso */
            }



        }, 2000); /* 2000 significa que haverá um atraso de 2000 milisegundos (ou 2 segundos) para puxar os dados do servido novamente*/



    }





    /* este bloco de código é executado quando o app já não está mais visível para o usuário */
    @Override
    public void onPause(){
        super.onPause();
        handler.removeCallbacksAndMessages(null); /* cancela o próximo código que seria executado, após os 3 segundos, fazendo com que o acesso ao banco de dados seja interrompido */
    }







    /* chamado todas as vezes que o botão é pressionado */
    @Override
    public void onClick(View v) {
        String text = editor.getText().toString();

        if(text.equals("")) /* se o editor estiver vazio, cancela a operação de envio */
            return;

        //text.replace(' ', '+'); /* troca todos os espaços pelo sinal de +, pois o servidor não aceita espaços */
        EnviarDados enviarDados = new EnviarDados();
        enviarDados.execute(text); /* envia os dados no editor de texto para o servidor, executando no plano de fundo */

        editor.setText(""); /* limpa o editor de texto depois do envio */
    }







    /**
     * Classe que nos permite acessar a internet rodando no plano de fundo do celular, sem fazer com que o app "trave".
     * Para isso, é necessário extender a classe AsyncTask, que faz este trabalho para nós.
     */
    private class PuxarDados extends AsyncTask<String, Void, String[]>{

        @Override
        protected String[] doInBackground(String... params) { /* tudo o que estiver aqui dentro rodará no plano de fundo */
            return Conexao.puxarConversa(); /* acessa a internet e joga os dados recebidos na String[] resultado */
        }







        @Override
        protected void onPostExecute(String[] resultado){

            int tamanhoDaConversa = resultado.length;
            String[] divisao; /* variável para receber a string dividida */

            /* vetores que irão nos ajudar a separar o autor da mensagem.
            *  O servidor devolve os dados no banco de dados da seguinte forma:
            *  autor:mensagem
            *  Por isso, usaremos o dois pontos para fazer esta separação.
            */
            String[] mensagem = new String[tamanhoDaConversa];
            String[] autor = new String[tamanhoDaConversa];

            for(int i = 0; i < tamanhoDaConversa; i++){
                divisao = resultado[i].split(":"); /* retorna dois vetores. O divisao[0] vai conter tudo antes do : e o divisao[1] vai conter tudo depois do : */
                autor[i] = divisao[0]; /* recebe tudo que está antes do : */
                mensagem[i] = divisao[1]; /* recebe tudo que está depois do : */
            }

            if(adapter == null){ /* se for a primeira vez que o listview está sendo usado, coloca o adaptador nele */
                adapter = new ListaAdapter(MainActivity.this, autor, mensagem); /* cria o adaptador */
                lista.setAdapter(adapter);
            }

            //Carrega conversa nova e atualiza a lista
            adapter.novaConversa(autor, mensagem); /* usa o adaptador antigo e coloca os novos dados na lista */
            adapter.notifyDataSetChanged(); /* atualiza o adaptador, atualizando a lista */
        }

    }

    private class EnviarDados extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            /* o servidor está configurado para receber os dados da seguinte maneira:
            * message=conversa+que+eu+digitei&author=autor+da+mensagem
            */
            Conexao.enviarConversa("message=" + params[0] + "&author=" + meuNome); /* envia dados */
            return null; /* retorna null pois não precisamos de nenhuma informação */

        }

    }







}
