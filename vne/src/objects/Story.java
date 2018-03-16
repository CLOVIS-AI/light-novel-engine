/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package objects;

import java.util.HashMap;
import vnscripts.validator.Commands;
import vnscripts.validator.SyntaxException;
import utils.resources.Resource;
import utils.resources.TextResource;

/**
 * This class represents a story ; with characters, chapters, backgrounds and more.
 * @author CLOVIS
 */
public class Story implements Save, Load {
    
    private final Resource directory;
    
    private final HashMap<String, Chapter> chapters
            = new HashMap<>();
    
    private final HashMap<String, Actor> actors
            = new HashMap<>();
    
    private final HashMap<String, Resource> sounds
            = new HashMap<>();
    
    private Progress state = null;
    
    private Settings settings;
    
    private final Commands commands;
    
    /**
     * Creates a Story without loading it.
     * <p>When the story will be loaded, it will use the default set of commands
     * ({@link Commands#DEFAULT}).
     * @param story the directory of the story
     * @see #load() Load this story
     * @see #Story(utils.resources.Resource, vnscripts.validator.Commands) Choose your commands
     */
    public Story(Resource story){
        this.directory = story;
        commands = Commands.DEFAULT;
    }
    
    /**
     * Creates a story without loading it.
     * <p>When the story will be loaded, it will use the provided set of
     * commands.
     * @param story the directory of the story
     * @param commands the set of commands you want to use
     * @see #Story(utils.resources.Resource) Use the default commands
     */
    public Story(Resource story, Commands commands){
        this.directory = story;
        this.commands = commands;
    }
    
    /**
     * Loads this story at the last save. Any unsaved progress will be lost.
     */
    @Override
    public void load() throws SyntaxException{
        reload();
    }
    
    /**
     * Loads the contents of the story without modifying the story's progress.
     */
    public void reload() throws SyntaxException{
        loadSettings();
        loadChapters();
        loadActors();
    }
    
    /**
     * Loads all chapters.
     * <p>The chapters will be located in the 'chapters' directory inside of the
     * story's root. Files that are not directories are ignored.
     */
    void loadChapters() throws SyntaxException{
        Resource chaptersFolder = directory.getChild("chapters");
        
        for(Resource chapter : chaptersFolder.getChildren())
            chapters.put(chapter.getName(), new Chapter(chapter, commands));
    }
    
    /**
     * Loads all musics.
     * <p>The sounds are expected to be located in the 'sounds/musics' directory
     * inside if the story's root.
     */
    void loadSounds(){
        Resource soundsFolder = directory
                .getChild("sounds")
                .getChild("musics");
        
        for(Resource music : soundsFolder.getChildren())
            sounds.put(music.getName(), music);
    }

    /**
     * Loads all actors.
     * <p>The actors are expected to be located in the 'actors' directory inside
     * of the story's root. Files that are not directories are ignored.
     */
    void loadActors() {
        Resource actorsFolder = directory.getChild("actors");
        
        for(Resource actor : actorsFolder.getChildren())
            actors.put(actor.getName(), new Actor(actor));
    }

    /**
     * Loads the settings of this story.
     * <p>The settings are expected to be located in the 'settings.txt' file
     * inside of the story's root.
     */
    void loadSettings() {
        TextResource settingsFile;
        try{
            settingsFile = (TextResource) directory.getChild("settings.txt");
        }catch(ClassCastException e){
            throw new IllegalArgumentException("The /settings.txt resource should be a text resource.");
        }
        
        settings = new Settings(settingsFile);
    }

    @Override
    public void save() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    /**
     * Gets a Chapter object.
     * @param name name of the chapter
     * @return The Chapter object.
     * @throws NullPointerException if no chapter with this name exist.
     */
    public Chapter getChapter(String name){
        if(chapters.containsKey(name))
            return chapters.get(name);
        else
            throw new NullPointerException("No chapter named '" + name + "' has been found.");
    }
}
