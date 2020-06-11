package br.com.gft.organizze.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import br.com.gft.organizze.R;
import br.com.gft.organizze.model.Movimentacao;

public class AdapterMovimentacao extends RecyclerView.Adapter<AdapterMovimentacao.MyViewHolder> {

    List<Movimentacao> listarMovimentacao;
    Context context;

    public AdapterMovimentacao(List<Movimentacao> lista, Context context){
        this.listarMovimentacao = lista;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_movimentacao, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Movimentacao movimentacao = listarMovimentacao.get(position);
        holder.descricao.setText(movimentacao.getDescricao());
        holder.categoria.setText(movimentacao.getCategoria());
        holder.valor.setText(String.format("R$ %.2f",movimentacao.getValor()));
        holder.valor.setTextColor(context.getResources().getColor(R.color.verde));


        if (movimentacao.getTipo().equals("d")){
            holder.valor.setTextColor(context.getResources().getColor(R.color.vermelho));
            holder.valor.setText("-"+String.format("R$ %.2f",movimentacao.getValor()));
        }
    }

    @Override
    public int getItemCount() {
        return listarMovimentacao.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView descricao, categoria, valor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            descricao = itemView.findViewById(R.id.descricao);
            categoria = itemView.findViewById(R.id.categoria);
            valor = itemView.findViewById(R.id.valor);
        }
    }
}
