package com.example.android.whatsappp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.widget.CircularProgressDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private List<Messages> userMessagesList;

    private FirebaseAuth mAuth;
    private DatabaseReference usersRef;

    public MessageAdapter(List<Messages> userMessagesList) {
        this.userMessagesList = userMessagesList;
    }


    public class MessageViewHolder extends RecyclerView.ViewHolder {

        public LinearLayout senderMessageLayout;
        public TextView senderMessageText, senderMessageTime;

        private LinearLayout receiverMessageLayout;
        public TextView receiverMessageText, receiverMessageTime;

        public LinearLayout messageSenderPictureLayout;
        public ImageView messageSenderPicture;
        public TextView messageSenderPictureTime;

        public LinearLayout messageReceiverPictureLayout;
        public ImageView messageReceiverPicture;
        public TextView messageReceiverPictureTime;

        public LinearLayout messageSenderDocLayout;
        public TextView messageSenderDocText, messageSenderDocTime;

        public LinearLayout messageReceiverDocLayout;
        public TextView messageReceiverDocText, messageReceiverDocTime;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMessageLayout = itemView.findViewById(R.id.sender_message_layout);
            senderMessageText = itemView.findViewById(R.id.sender_message_text);
            senderMessageTime = itemView.findViewById(R.id.sender_message_time);

            receiverMessageLayout = itemView.findViewById(R.id.receiver_message_layout);
            receiverMessageText = itemView.findViewById(R.id.receiver_message_text);
            receiverMessageTime = itemView.findViewById(R.id.receiver_message_time);

            messageReceiverPictureLayout = itemView.findViewById(R.id.message_receiver_image_view_layout);
            messageReceiverPicture = itemView.findViewById(R.id.message_receiver_image_view);
            messageReceiverPictureTime = itemView.findViewById(R.id.message_receiver_image_view_time);

            messageSenderPictureLayout = itemView.findViewById(R.id.message_sender_image_view_layout);
            messageSenderPicture = itemView.findViewById(R.id.message_sender_image_view);
            messageSenderPictureTime = itemView.findViewById(R.id.message_sender_image_view_time);

            messageSenderDocLayout = itemView.findViewById(R.id.message_sender_doc_layout);
            messageSenderDocText = itemView.findViewById(R.id.message_sender_doc_text);
            messageSenderDocTime = itemView.findViewById(R.id.message_sender_doc_time);

            messageReceiverDocLayout = itemView.findViewById(R.id.message_receiver_doc_layout);
            messageReceiverDocText = itemView.findViewById(R.id.message_receiver_doc_text);
            messageReceiverDocTime = itemView.findViewById(R.id.message_receiver_doc_time);

        }
    }


    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_messages_layout, viewGroup, false);

        mAuth = FirebaseAuth.getInstance();

        return new MessageViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final MessageViewHolder messageViewHolder, final int i) {

        String messageSenderId = mAuth.getCurrentUser().getUid();
        Messages messages = userMessagesList.get(i);

        String fromUserId = messages.getFrom();
        String fromMessageType = messages.getType();

        usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(fromUserId);

        messageViewHolder.senderMessageLayout.setVisibility(View.GONE);
        messageViewHolder.receiverMessageLayout.setVisibility(View.GONE);

        messageViewHolder.messageSenderPictureLayout.setVisibility(View.GONE);
        messageViewHolder.messageReceiverPictureLayout.setVisibility(View.GONE);

        messageViewHolder.messageSenderDocLayout.setVisibility(View.GONE);
        messageViewHolder.messageReceiverDocLayout.setVisibility(View.GONE);

        if (fromMessageType.equals("text")) {

            if (fromUserId.equals(messageSenderId)) {
                messageViewHolder.senderMessageLayout.setVisibility(View.VISIBLE);

                String text =  messages.getDate() + "  " + messages.getTime();
                messageViewHolder.senderMessageText.setText(messages.getMessage());
                messageViewHolder.senderMessageTime.setText(text);

                messageViewHolder.senderMessageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogSenderText(i, messageViewHolder);
                    }
                });


            } else {
                messageViewHolder.receiverMessageLayout.setVisibility(View.VISIBLE);

                String text =  messages.getDate() + "  " + messages.getTime();
                messageViewHolder.receiverMessageText.setText(messages.getMessage());
                messageViewHolder.receiverMessageTime.setText(text);

                messageViewHolder.receiverMessageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogReceiverText(i, messageViewHolder);
                    }
                });

            }

        } else if (fromMessageType.equals("image")) {

            CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(messageViewHolder.itemView.getContext());
            circularProgressDrawable.setStrokeWidth(10);
            circularProgressDrawable.setCenterRadius(60);
            circularProgressDrawable.start() ;

            if (fromUserId.equals(messageSenderId)) {
                messageViewHolder.messageSenderPictureLayout.setVisibility(View.VISIBLE);

                String text =  messages.getDate() + "  " + messages.getTime();
                messageViewHolder.messageSenderPictureTime.setText(text);

                Glide.with(messageViewHolder.itemView.getContext()).load(messages.getMessage())
                        .placeholder(circularProgressDrawable).into(messageViewHolder.messageSenderPicture);

                messageViewHolder.messageSenderPictureLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogSenderImage(i, messageViewHolder);
                    }
                });

            } else {
                messageViewHolder.messageReceiverPictureLayout.setVisibility(View.VISIBLE);

                String text =  messages.getDate() + "  " + messages.getTime();
                messageViewHolder.messageReceiverPictureTime.setText(text);

                Glide.with(messageViewHolder.itemView.getContext()).load(messages.getMessage())
                        .placeholder(circularProgressDrawable).into(messageViewHolder.messageReceiverPicture);

                messageViewHolder.messageReceiverPictureLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogReceiverImage(i, messageViewHolder);
                    }
                });

            }
        } else if (fromMessageType.equals("pdf") || fromMessageType.equals("docx")) {
            if (fromUserId.equals(messageSenderId)) {
                messageViewHolder.messageSenderDocLayout.setVisibility(View.VISIBLE);

                String text =  messages.getDate() + "  " + messages.getTime();
                messageViewHolder.messageSenderDocTime.setText(text);

                if(fromMessageType.equals("pdf")){
                    messageViewHolder.messageSenderDocText.setText("PDF");
                }
                else{
                    messageViewHolder.messageSenderDocText.setText("DOC");
                }

                messageViewHolder.messageSenderDocLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogSenderDoc(i, messageViewHolder);
                    }
                });


            } else {
                messageViewHolder.messageReceiverDocLayout.setVisibility(View.VISIBLE);

                String text =  messages.getDate() + "  " + messages.getTime();
                messageViewHolder.messageReceiverDocTime.setText(text);

                if(fromMessageType.equals("pdf")){
                    messageViewHolder.messageReceiverDocText.setText("PDF");
                }
                else{
                    messageViewHolder.messageReceiverDocText.setText("DOC");
                }

                messageViewHolder.messageReceiverDocLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        alertDialogReceiverDoc(i, messageViewHolder);
                    }
                });


            }
        }


    }


    @Override
    public int getItemCount() {
        return userMessagesList.size();
    }

    public void setUserMessagesList(){

        notifyDataSetChanged();
    }


    private void alertDialogSenderText(final int position, final MessageViewHolder messageViewHolder){

        CharSequence options[] = new CharSequence[]{
                "Delete For Me",
                "Cancel",
                "Delete For Everyone"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
        builder.setTitle("Delete Message");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0) {
                    deleteSentMessage(position, messageViewHolder);
                } else if (i == 1) {
                    dialogInterface.cancel();
                } else if (i == 2) {
                    deleteMessageForEveryone(position, messageViewHolder);
                }


            }
        });
        builder.show();

    }

    private void alertDialogReceiverText(final int position, final MessageViewHolder messageViewHolder){

        CharSequence options[] = new CharSequence[]{
                "Delete For Me",
                "Cancel"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
        builder.setTitle("Delete Message");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0) {
                    deleteReceivedMessage(position, messageViewHolder);
                } else if (i == 1) {
                    dialogInterface.cancel();
                }


            }
        });
        builder.show();

    }


    private void alertDialogSenderImage(final int position, final MessageViewHolder messageViewHolder){

        CharSequence options[] = new CharSequence[]{
                "Delete For Me",
                "Open",
                "Cancel",
                "Delete For Everyone"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
        builder.setTitle("Delete Image");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {

                if (i == 0) {
                    deleteSentMessage(position, messageViewHolder);
                } else if (i == 1) {

                    Intent intent = new Intent(messageViewHolder.itemView.getContext(), ImageViewerActivity.class);
                    intent.putExtra("url", userMessagesList.get(position).getMessage());
                    messageViewHolder.itemView.getContext().startActivity(intent);

                } else if (i == 2) {
                    dialogInterface.cancel();
                } else if (i == 3) {
                    deleteMessageForEveryone(position, messageViewHolder);
                }


            }
        });
        builder.show();

    }

    private void alertDialogReceiverImage(final int position, final MessageViewHolder messageViewHolder){

        CharSequence options[] = new CharSequence[]{
                "Delete For Me",
                "Open",
                "Cancel"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
        builder.setTitle("Delete Image");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {

                if (i == 0) {
                    deleteReceivedMessage(position, messageViewHolder);
                } else if (i == 1) {

                    Intent intent = new Intent(messageViewHolder.itemView.getContext(), ImageViewerActivity.class);
                    intent.putExtra("url", userMessagesList.get(position).getMessage());
                    messageViewHolder.itemView.getContext().startActivity(intent);

                } else if (i == 2) {
                    dialogInterface.cancel();
                }

            }
        });
        builder.show();

    }


    private void alertDialogSenderDoc(final int position, final MessageViewHolder messageViewHolder){

        CharSequence options[] = new CharSequence[]{
                "Delete For Me",
                "Open",
                "Cancel",
                "Delete For Everyone"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
        builder.setTitle("Delete Document");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {

                if (i == 0) {
                    deleteSentMessage(position, messageViewHolder);
                } else if (i == 1) {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                    messageViewHolder.itemView.getContext().startActivity(intent);

                } else if (i == 2) {
                    dialogInterface.cancel();
                } else if (i == 3) {
                    deleteMessageForEveryone(position, messageViewHolder);
                }

            }
        });
        builder.show();

    }

    private void alertDialogReceiverDoc(final int position, final MessageViewHolder messageViewHolder){

        CharSequence options[] = new CharSequence[]{
                "Delete For Me",
                "Open",
                "Cancel"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(messageViewHolder.itemView.getContext());
        builder.setTitle("Delete Document");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, final int i) {

                if (i == 0) {
                    deleteReceivedMessage(position, messageViewHolder);
                } else if (i == 1) {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(userMessagesList.get(position).getMessage()));
                    messageViewHolder.itemView.getContext().startActivity(intent);

                } else if (i == 2) {
                    dialogInterface.cancel();
                }

            }
        });
        builder.show();

    }


    private void deleteSentMessage(final int position, final MessageViewHolder holder) {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Messages").child(userMessagesList.get(position).getFrom())
                .child(userMessagesList.get(position).getTo())
                .child(userMessagesList.get(position).getMessageId())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(holder.itemView.getContext(), "Message Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Error", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        notifyDataSetChanged();

    }

    private void deleteReceivedMessage(final int position, final MessageViewHolder holder) {

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Messages").child(userMessagesList.get(position).getTo())
                .child(userMessagesList.get(position).getFrom())
                .child(userMessagesList.get(position).getMessageId())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(holder.itemView.getContext(), "Message Deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(holder.itemView.getContext(), "Error", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        notifyDataSetChanged();

    }

    private void deleteMessageForEveryone(final int position, final MessageViewHolder holder) {

        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.child("Messages").child(userMessagesList.get(position).getFrom())
                .child(userMessagesList.get(position).getTo())
                .child(userMessagesList.get(position).getMessageId())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    rootRef.child("Messages").child(userMessagesList.get(position).getTo())
                            .child(userMessagesList.get(position).getFrom())
                            .child(userMessagesList.get(position).getMessageId())
                            .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(holder.itemView.getContext(), "Message Deleted", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });


                } else {
                    Toast.makeText(holder.itemView.getContext(), "Error", Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(holder.itemView.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        notifyDataSetChanged();

    }


}
