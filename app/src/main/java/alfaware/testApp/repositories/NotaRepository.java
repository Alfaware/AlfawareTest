package alfaware.testApp.repositories;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import alfaware.testApp.abstract_classes.ListResult;
import alfaware.testApp.abstract_classes.Result;
import alfaware.testApp.entities.Nota;
import alfaware.testApp.firebase.FirebaseReference;

public class NotaRepository {

    private static NotaRepository instance;

    public static NotaRepository getInstance() {
        if (instance == null) {
            instance = new NotaRepository();
        }
        return instance;
    }

    //==============================================================================================

    public void subscribeList(MutableLiveData<ListResult<Nota>> notas) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FirebaseReference.NOTAS);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notas.getValue().getResult().getData().clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Nota nota = snapshot.getValue(Nota.class);
                    nota.setId(snapshot.getKey());
                    notas.getValue().getResult().getData().add(nota);
                }
                notas.postValue(notas.getValue());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                notas.getValue().getResult().setException(databaseError.toException());
                notas.postValue(notas.getValue());
            }
        });
    }

    public void add(MutableLiveData<Result<Nota>> nota) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FirebaseReference.NOTAS);
        reference.push().setValue(nota.getValue().getResult().getData()).addOnSuccessListener(aVoid -> nota.postValue(nota.getValue())).addOnFailureListener(e -> {
            nota.getValue().getResult().setException(e);
            nota.postValue(nota.getValue());
        });
    }
}
