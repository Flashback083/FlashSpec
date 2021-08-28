package fr.flashback083.flashspec.PaginationApi;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;

import javax.annotation.Nonnull;
import java.util.*;

public class PaginationApi {

    public static HashMap<UUID, HashMap<String,Integer>> actualPage = new HashMap<>();
    public static HashMap<String,Integer> playerpage = new HashMap<>();
    public static HashMap<UUID,PaginationApi> paginationlist = new HashMap<>();
    private UUID uuid;
    private List<ITextComponent> texts;
    private int linesPerPage;
    private ITextComponent header,footer,title;

    public static class PaginationBuilder {

        private List<ITextComponent> texts;
        private ITextComponent header,footer,title;
        private int linesPerPage;
        //private ITextComponent header;
        private UUID uuid;
        //public static HashMap<UUID,PaginationApi> paginationlist = new HashMap<>();

        public PaginationBuilder(@Nonnull UUID uuid){
            this.uuid = uuid;
        }

        public PaginationBuilder setTexts(@Nonnull List<ITextComponent> texts){
            this.texts = texts;
            return this;
        }

        public PaginationBuilder setHeader(ITextComponent header){
            this.header = header;
            return this;
        }

        public PaginationBuilder setFooter(ITextComponent footer){
            this.footer = footer;
            return this;
        }

        public PaginationBuilder setLinesPerPage(int linesPerPage){
            this.linesPerPage = linesPerPage;
            return this;
        }


        public PaginationBuilder setTitle(ITextComponent title){
            this.title = title;
            return this;
        }

        public PaginationApi build(){
            PaginationApi pagination = new PaginationApi();
            pagination.texts = this.texts;
            pagination.linesPerPage = this.linesPerPage;
            pagination.uuid = this.uuid;
            pagination.header = this.header;
            pagination.footer = this.footer;
            pagination.title = this.title;
            paginationlist.put(this.uuid,pagination);
            return pagination;
        }

        public void sendTo(List<EntityPlayerMP> players){
            PaginationApi pagination = new PaginationBuilder(this.uuid).setTexts(this.texts).setLinesPerPage(this.linesPerPage).setFooter(this.footer).setHeader(this.header).setTitle(this.title).build();
            players.forEach(player -> sendNewPage(player,pagination,1));
        }
        public void sendTo(EntityPlayerMP player){
            PaginationApi pagination = new PaginationBuilder(this.uuid).setTexts(this.texts).setLinesPerPage(this.linesPerPage).setFooter(this.footer).setHeader(this.header).setTitle(this.title).build();
            sendNewPage(player,pagination,1);
        }

    }

    public static PaginationApi getPaginationFromUUID(UUID uuid){
        return paginationlist.get(uuid);
    }


    public void sendTo(List<EntityPlayerMP> players){
        PaginationApi pagination = new PaginationBuilder(this.uuid).setTexts(this.texts).setLinesPerPage(this.linesPerPage).setFooter(this.footer).setHeader(this.header).setTitle(this.title).build();
        players.forEach(player -> sendNewPage(player,pagination,1));
    }

    public void sendTo(EntityPlayerMP player){
        PaginationApi pagination = new PaginationBuilder(this.uuid).setTexts(this.texts).setLinesPerPage(this.linesPerPage).setFooter(this.footer).setHeader(this.header).setTitle(this.title).build();
        sendNewPage(player,pagination,1);
    }

    public UUID getUUID(){
        return this.uuid;
    }

    public List<ITextComponent> getContents(){
        return this.texts;
    }

    public int getLinesPerPage( ){
        return this.linesPerPage;
    }

    public ITextComponent getTitle(){
        return this.title;
    }

    public ITextComponent getHeader(){
        return this.header;
    }

    public ITextComponent getFooter(){
        return this.footer;
    }

    public void sendTo(List<EntityPlayerMP> players, PaginationApi pagination, int page){
        players.forEach(player -> {
            sendNewPage(player,pagination,page);
        });
    }

    public static void sendNextPage(EntityPlayerMP player, PaginationApi pagination){
        int nextpage = actualPage.get(pagination.getUUID()).get(player.getName()) + 1;
        sendNewPage(player,pagination,nextpage);
    }


    public static void sendPreviousPage(EntityPlayerMP player, PaginationApi pagination){
        int previouspage = actualPage.get(pagination.uuid).get(player.getName()) - 1 ;
        sendNewPage(player,pagination,previouspage);
    }



    private static List<ITextComponent> getTextsAtPage(PaginationApi pagination, int page){
        List<ITextComponent> listtext = pagination.texts;
        List<List<ITextComponent>> allpages = getPages(listtext, pagination.linesPerPage);
        if (page == 0 || page == allpages.size()+1){
            return null;
        }else {
            return allpages.get(page-1);
        }
    }

    public static <T> List<List<T>> getPages(Collection<T> c, Integer pageSize) {
        if (c == null)
            return Collections.emptyList();
        List<T> list = new ArrayList<T>(c);
        if (pageSize == null || pageSize <= 0 || pageSize > list.size())
            pageSize = list.size();
        int numPages = (int) Math.ceil((double)list.size() / (double)pageSize);
        List<List<T>> pages = new ArrayList<>(numPages);
        for (int pageNum = 0; pageNum < numPages;)
            pages.add(list.subList(pageNum * pageSize, Math.min(++pageNum * pageSize, list.size())));
        return pages;
    }


    private static void sendNewPage(EntityPlayerMP player, PaginationApi pagination, int page){
        List<ITextComponent> listtext = getTextsAtPage(pagination,page);
        if (pagination.title != null) listtext.add(0,pagination.getTitle());
        if (pagination.header != null) listtext.add(1,pagination.getHeader());
        ITextComponent text = new TextComponentString("");
        listtext.forEach(txt-> text.appendSibling(txt).appendText("\n"));
        TextComponentString previous = new TextComponentString(TextFormatting.BLUE + ""+ TextFormatting.BOLD + "X 1");
        ITextComponent next = new TextComponentString(TextFormatting.BLUE + ""+ TextFormatting.BOLD + page + " X");
        if (getTextsAtPage(pagination,page-1) != null){
            previous = new TextComponentString(TextFormatting.BLUE + ""+ TextFormatting.BOLD + "<< " + page);
            previous.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/flashspeccallback " + pagination.getUUID() + " " + player.getName() + " previous"));
        }
        if (getTextsAtPage(pagination,page + 1) != null){
            next = new TextComponentString(TextFormatting.BLUE + ""+ TextFormatting.BOLD + (page+1)+" >>");
            next.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/flashspeccallback " + pagination.getUUID() + " " + player.getName() + " next"));
        }
        ITextComponent changepage = previous.appendText(TextFormatting.BLUE + ""+ TextFormatting.BOLD + "/").appendSibling(next);
        text.appendSibling(changepage);
        if (pagination.footer != null) text.appendText("\n").appendSibling(pagination.getFooter());
        playerpage.put(player.getName(),page);
        actualPage.put(pagination.uuid,playerpage);
        player.sendMessage(text);
    }




}
