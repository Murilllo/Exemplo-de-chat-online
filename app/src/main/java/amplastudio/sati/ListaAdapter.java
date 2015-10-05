package amplastudio.sati;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Murillo on 05/10/2015.
 */
public class ListaAdapter extends BaseAdapter {

    String[] texto1;
    String[] texto2;

    TextView textoDeCima;
    TextView textoDeBaixo;

    LayoutInflater inflador;

    public ListaAdapter(Context context, String[] txt1, String[] txt2){
        texto1 = txt1;
        texto2 = txt2;

        inflador = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void novaConversa(String[] txt1, String[] txt2){
        texto1 = txt1;
        texto2 = txt2;
    }

    @Override
    public int getCount() {

        return texto1.length; /* devolve o número de elementos da lista que serão criados */

    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /* Chamado toda a vez que um elemento está sendo criado pela ListView
     * Se houver, por exemplo, 50 elementos para serem criados, position vai variar de 0 a 49
     * A variável position mostra o número da linha da nossa lista
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(texto1[position].equals(MainActivity.meuNome)) /* se você for o dono da mensagem, carrega o layout onde os textos estão à direita */
            convertView = inflador.inflate(R.layout.linha_lista_direita, parent, false);
        else
            convertView = inflador.inflate(R.layout.linha_lista, parent, false); /* caso contrário, cria a janela onde os textos estão à esquerda */

        /* Cncontra os textos que está dentro da janela criada e passada para a variavel convertView *
         * Como os textos de ambas as janelas que podem ser criadas na linha de cima
         */
        textoDeCima = (TextView) convertView.findViewById(R.id.texto1);
        textoDeBaixo = (TextView) convertView.findViewById(R.id.texto2);

        textoDeCima.setText(texto1[position]);
        textoDeBaixo.setText(texto2[position]);

        return convertView;

    }
}
