package me.refluxo.serverlibrary.listeners;

import me.refluxo.serverlibrary.ServerLibrary;
import me.refluxo.serverlibrary.util.player.PlayerAPI;
import me.refluxo.serverlibrary.util.player.PlayerManager;
import me.refluxo.serverlibrary.util.player.bad.BadWords;
import net.md_5.bungee.api.ChatMessageType;
import net.ricecode.similarity.DiceCoefficientStrategy;
import net.ricecode.similarity.JaroWinklerStrategy;
import net.ricecode.similarity.SimilarityStrategy;
import net.ricecode.similarity.StringSimilarityServiceImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import xyz.plocki.asyncthread.AsyncThread;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

public class ChatEvent implements Listener {

    private static final Map<Player, Long> time = new HashMap<>();
    private static final Map<Player, String> lastMessage = new HashMap<>();

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        //anti spam
        String msg = event.getMessage();
        PlayerAPI playerAPI = new PlayerManager().getPlayer(event.getPlayer());
        if(!((System.currentTimeMillis() - time.getOrDefault(event.getPlayer(), System.currentTimeMillis()-5000)) < 3000)) {
            time.put(event.getPlayer(), System.currentTimeMillis());
            //similarity
            if (lastMessage.containsKey(event.getPlayer())) {
                if(new StringSimilarityServiceImpl(new JaroWinklerStrategy()).score(lastMessage.get(event.getPlayer()), event.getMessage()) >=0.90) {
                    event.setCancelled(true);
                    playerAPI.sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.ACTION_BAR, ServerLibrary.prefix + "Deine Nachricht war zu ähnlich, zu deiner vorherigen Nachricht.");
                    return;
                }
            }
            lastMessage.put(event.getPlayer(), event.getMessage());

            String finalMsg1 = msg;
            BadWords.badWords.forEach(word -> {
                String[] splitMsg = finalMsg1.split("\\s+");
                Arrays.stream(splitMsg).toList().forEach(msgWord -> {
                    if(new StringSimilarityServiceImpl(new JaroWinklerStrategy()).score(word, msgWord) >= 0.85) {
                        event.setCancelled(true);
                        playerAPI.sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.ACTION_BAR, ServerLibrary.prefix+"Bitte achte auf dein Chatverhalten (Wortwahl).");
                    }
                });
            });
            if(event.isCancelled()) {
                return;
            }
            //anti caps
            if(msg.length() >= 10) {
                int up = 0;
                for(int i = 0; i <= msg.length(); i++) {
                    if(Character.isUpperCase(i)) {
                        up++;
                    }
                }
                if(up >= msg.length()/2) {
                    event.setCancelled(true);
                    playerAPI.sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.ACTION_BAR, ServerLibrary.prefix+"Bitte achte auf dein Chatverhalten (Caps).");
                    return;
                }
            }
            //anti advert
            if(msg.toLowerCase().contains("http://") || msg.toLowerCase().contains("https://")) {
                msg = msg.replaceAll("/\\s/g", "");
            }

            //anti harassment/bad words
            if(BadWords.contains(msg)) {
                event.setCancelled(true);
                playerAPI.sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.ACTION_BAR, ServerLibrary.prefix+"Bitte achte auf dein Chatverhalten (Wortwahl).");
                return;
            }
            if(msg.toLowerCase().contains("pride") || msg.toLowerCase().contains("lgbt") || msg.toLowerCase().contains("gay") || msg.toLowerCase().contains("schwul") || msg.toLowerCase().contains("lesbe") || msg.toLowerCase().contains("lesbisch") || msg.toLowerCase().contains("bisexuel") || msg.toLowerCase().contains("trans*") || msg.toLowerCase().contains("transgender")) {
                playerAPI.sendMessage(PlayerAPI.MessageType.WARNING, ChatMessageType.CHAT , ServerLibrary.prefix + "Deine Nachricht wurde zur Überprüfung gespeichert um sicherzustellen, dass diese in keinem negativen Kontext benutzt wurde.");
                new PlayerManager().getPlayer(event.getPlayer()).log("Wrote \"" + msg + "\" in the chat.");
            }
            if(event.isCancelled()) {
                return;
            }
        } else {
            event.setCancelled(true);
            playerAPI.sendMessage(PlayerAPI.MessageType.ERROR, ChatMessageType.ACTION_BAR, ServerLibrary.prefix+"Du musst 3 Sekunden zwischen einer Nachricht warten.");
            return;
        }
        if(!event.isCancelled()) {
            AtomicReference<String> finalMsg = new AtomicReference<>(msg);
            if(finalMsg.get().contains("&")) {
                if(event.getPlayer().hasPermission("refluxo.chat.color")) {
                    finalMsg.set(finalMsg.get().replace("&", "§"));
                } else {
                    new PlayerManager().getOnlinePlayers().forEach(player -> {
                        if(finalMsg.get().contains(player.getAPIPlayer().getName())) {
                            finalMsg.set("§7" + finalMsg.get().replace(player.getAPIPlayer().getName(), "§b@" + player.getAPIPlayer().getName() + "§7"));
                            player.sendMessage(PlayerAPI.MessageType.ANNOUNCEMENT, ChatMessageType.ACTION_BAR, ServerLibrary.prefix+"Du wurdest von §b"+event.getPlayer().getName()+" §7im Chat gepingt.");
                        }
                    });
                }
            } else {
                new PlayerManager().getOnlinePlayers().forEach(player -> {
                    if(finalMsg.get().contains(player.getAPIPlayer().getName())) {
                        finalMsg.set("§7" + finalMsg.get().replace(player.getAPIPlayer().getName(), "§b@" + player.getAPIPlayer().getName() + "§7"));
                        player.sendMessage(PlayerAPI.MessageType.ANNOUNCEMENT, ChatMessageType.ACTION_BAR, ServerLibrary.prefix+"Du wurdest von §b"+event.getPlayer().getName()+" §7im Chat gepingt.");
                    }
                });
            }
            if(event.getPlayer().hasPermission("refluxo.ping.everyone")) {
                if(finalMsg.get().toLowerCase().startsWith("announce_everyone ")) {
                    finalMsg.set(finalMsg.get().replaceFirst("announce_everyone ", ""));
                    event.setCancelled(true);
                    new PlayerManager().getOnlinePlayers().forEach(playerAPI1 -> {
                        playerAPI1.sendMessage(PlayerAPI.MessageType.ANNOUNCEMENT, ChatMessageType.ACTION_BAR, finalMsg.get());
                        new AsyncThread(() -> playerAPI1.sendMessage(PlayerAPI.MessageType.ANNOUNCEMENT, ChatMessageType.ACTION_BAR, finalMsg.get())).runAsyncTaskLater(1);
                        new AsyncThread(() -> playerAPI1.sendMessage(PlayerAPI.MessageType.ANNOUNCEMENT, ChatMessageType.ACTION_BAR, finalMsg.get())).runAsyncTaskLater(2);
                    });
                    return;
                }
            }
            if(!event.isCancelled()) {
                event.setFormat(new PlayerManager().getPlayer(event.getPlayer()).getAPIPlayer().getRank().getPrefix() + "§7" + event.getPlayer().getName() + " §8» §7" + finalMsg.get());
            }
        }
    }

}
