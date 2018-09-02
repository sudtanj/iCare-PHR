package sud_tanj.com.icare.Frontend.Fragment.DataCatalogue.DataDetail;

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;
import co.intentservice.chatui.models.ChatMessage.Type;
import lombok.AllArgsConstructor;
import sud_tanj.com.icare.Backend.Database.PersonalData.DataComment;

/**
 * This class is part of iCare Project
 * Any modified within this class without reading the
 * manual will cause problem!
 * <p>
 * Created by Sudono Tanjung on 02/09/2018 - 16:23.
 * <p>
 * This class last modified by User
 */
@AllArgsConstructor
public class CommentEventListener implements ValueEventListener{

    private ChatView chatView;

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        chatView.clearMessages();
        for (DataSnapshot temp : dataSnapshot.getChildren()) {
            DataComment dataComment=temp.getValue(DataComment.class);
            ChatMessage chatMessage=new ChatMessage(dataComment.getMessage(),dataComment.getTimeStamp(), Type.SENT);
            if(dataComment.getCommentType().equals(DataComment.COMMENT_BY_INDIVIDUAL)){
                chatMessage.setSender("Individual");
            }
            else {
                chatMessage.setSender("Doctor");
                chatMessage.setType(Type.RECEIVED);
            }
            chatView.addMessage(chatMessage);
        }
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
