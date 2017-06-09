package com.st33fo.glideforktt;

import android.content.Context;
import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Stefan on 6/8/2017.
 */

public class EmojiListCreator {

    public static List<EmojiObject> PopulateList(Context context){
        List<EmojiObject> list = new ArrayList<EmojiObject>();
        Resources res = context.getResources();
        String[] tempEmojiNames = res.getStringArray(R.array.kttemojis);
        int[] emojiimages = {R.drawable.smileyamp,R.drawable.cryamp,R.drawable.agreed,
                R.drawable.aliveclap, R.drawable.aliveparrot, R.drawable.allears, R.drawable.amber,
                R.drawable.atiavy, R.drawable.awesome, R.drawable.banderas, R.drawable.banjo, R.drawable.banplz,
                R.drawable.bear, R.drawable.beat, R.drawable.binowhat, R.drawable.bitchgtfo, R.drawable.bluetard,
                R.drawable.bronwhat, R.drawable.bruh, R.drawable.button, R.drawable.bye2, R.drawable.bye, R.drawable.camby,
                R.drawable.camvp, R.drawable.clap, R.drawable.cmon, R.drawable.cold, R.drawable.colenice, R.drawable.cosby,
                R.drawable.cry, R.drawable.cryfam, R.drawable.cudifly,R.drawable.dfrown, R.drawable.dab, R.drawable.dahell, R.drawable.daisyscust,
                R.drawable.damn, R.drawable.dash, R.drawable.datass, R.drawable.dead2, R.drawable.dead, R.drawable.delfap, R.drawable.denzel,
                R.drawable.dk, R.drawable.dno, R.drawable.doge, R.drawable.dontcare, R.drawable.drakehah, R.drawable.drakelaff2, R.drawable.drakelaff,
                R.drawable.drakewhat, R.drawable.dw, R.drawable.epic, R.drawable.eyeroll, R.drawable.facepalm2, R.drawable.facepalm, R.drawable.fck,
                R.drawable.feedme, R.drawable.ffpu, R.drawable.fire, R.drawable.flames, R.drawable.floyd, R.drawable.fly, R.drawable.foh, R.drawable.frankwd,
                R.drawable.fuu, R.drawable.getout, R.drawable.ghey, R.drawable.golfclap, R.drawable.h5, R.drawable.hah2, R.drawable.hah3, R.drawable.hah,
                R.drawable.happyhug, R.drawable.hat, R.drawable.haw, R.drawable.he, R.drawable.hhh, R.drawable.hnng, R.drawable.hug, R.drawable.huh,
                R.drawable.jayhuh, R.drawable.jaylaff, R.drawable.jayout, R.drawable.jo, R.drawable.joe, R.drawable.jordancry, R.drawable.jordanlaff,
                R.drawable.jordanok, R.drawable.jordanscowl, R.drawable.kanyeoh, R.drawable.kanyeshh, R.drawable.kanyeshrug, R.drawable.keefnah,
                R.drawable.kendricklaff, R.drawable.kermit2, R.drawable.kermit, R.drawable.khaled2, R.drawable.khaled, R.drawable.koolaid,
                R.drawable.laugh, R.drawable.lebrontroll, R.drawable.leo, R.drawable.lilb, R.drawable.lol, R.drawable.lying, R.drawable.mad,
                R.drawable.martin, R.drawable.marty, R.drawable.maybe, R.drawable.meek, R.drawable.melosalute, R.drawable.mhmm, R.drawable.mindfk,
                R.drawable.mindfkd, R.drawable.mm, R.drawable.mrkrabs, R.drawable.mystery, R.drawable.nas, R.drawable.newyear, R.drawable.ninja,
                R.drawable.northout, R.drawable.notsureif, R.drawable.noudidnt, R.drawable.obama, R.drawable.obamahhh, R.drawable.oblivious, R.drawable.offtopic,
                R.drawable.oh2, R.drawable.oh, R.drawable.ohno, R.drawable.ohnorth, R.drawable.ohtu, R.drawable.okay, R.drawable.omg, R.drawable.omgfap, R.drawable.paranoid,
                R.drawable.parrot, R.drawable.peekaboo, R.drawable.perv, R.drawable.pog, R.drawable.pokerface, R.drawable.popcorn, R.drawable.prince, R.drawable.psyboom,
                R.drawable.puke, R.drawable.randall, R.drawable.rejoice, R.drawable.ridin, R.drawable.robo, R.drawable.russ, R.drawable.sadblu, R.drawable.sage, R.drawable.salt,
                R.drawable.salute, R.drawable.sarc2, R.drawable.scoobyscust, R.drawable.seanshrug, R.drawable.shame, R.drawable.shh, R.drawable.siren, R.drawable.slick, R.drawable.smh, R.drawable.smug,
                R.drawable.snifle, R.drawable.snoopsmh, R.drawable.sonicshrug, R.drawable.soon, R.drawable.stronger, R.drawable.tash, R.drawable.travlaff, R.drawable.tswift, R.drawable.tumble, R.drawable.twisted,
                R.drawable.tylee, R.drawable.tyrone, R.drawable.vom, R.drawable.waiting, R.drawable.wd, R.drawable.we, R.drawable.wellinever, R.drawable.what, R.drawable.whew, R.drawable.whoa, R.drawable.wilma,
                R.drawable.wizhhh, R.drawable.wom, R.drawable.word, R.drawable.work, R.drawable.worship, R.drawable.wtf, R.drawable.wut, R.drawable.yar, R.drawable.yawn, R.drawable.yay, R.drawable.yejoice, R.drawable.yesip,
                R.drawable.yewhat, R.drawable.yonce, R.drawable.youknow,R.drawable.angryface,R.drawable.embarrassedcolondashsquarebracket,R.drawable.heart,R.drawable.rightcarrotunderscorerightcarrot,R.drawable.lipsrsealedcolondash,
                R.drawable.schockedfacecolono,R.drawable.smilecolon,R.drawable.smileyface,R.drawable.undecidedcolondashbackslash,R.drawable.wink};

        for(int i =0; i<210;i++)
        {
            EmojiObject tempemojiObject = new EmojiObject(emojiimages[i],tempEmojiNames[i]);
            list.add(tempemojiObject);
        }

        return list;
    }
}
