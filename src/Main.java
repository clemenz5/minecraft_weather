import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import org.openweathermap.api.DataWeatherClient;
import org.openweathermap.api.UrlConnectionDataWeatherClient;
import org.openweathermap.api.model.currentweather.CurrentWeather;
import org.openweathermap.api.query.*;
import org.openweathermap.api.query.currentweather.CurrentWeatherOneLocationQuery;

public class Main extends JavaPlugin implements CommandExecutor {
    DataWeatherClient client;
    World world;

    public Main() {
        client = new UrlConnectionDataWeatherClient("fd9e7de10db46dc4ba79f3568dcaf468");
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("§aweather plugin disabled");
    }

    @Override
    public void onEnable() {
        Bukkit.getConsoleSender().sendMessage("§cweather plugin enabled");
        world = Bukkit.getWorlds().get(0);
        this.getCommand("change_weather").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            return false;
        } else {
            String location = args[0];
            Bukkit.getConsoleSender().sendMessage(args[0]);
            CurrentWeatherOneLocationQuery currentWeatherOneLocationQuery = QueryBuilderPicker.pick()
                    .currentWeather()                   // get current weather
                    .oneLocation()                      // for one location
                    .byCityName(location)
                    .countryCode("DE")
                    .type(Type.ACCURATE)                // with Accurate search
                    .language(Language.ENGLISH)         // in English language
                    .responseFormat(ResponseFormat.JSON)// with JSON response format
                    .unitFormat(UnitFormat.METRIC)      // in metric units
                    .build();
            CurrentWeather currentWeather = client.getCurrentWeather(currentWeatherOneLocationQuery);
            Bukkit.getConsoleSender().sendMessage(currentWeather.getMainParameters().getHumidity()+"");
            if(currentWeather != null){
                if (currentWeather.getMainParameters().getHumidity() > 60) {
                    world.setStorm(true);
                } else {
                    world.setStorm(false);
                }
                if(currentWeather.getMainParameters().getPressure() > 1100){
                    world.setThundering(true);
                }else{
                    world.setThundering(false);
                }
                return true;
            }else{
                return false;
            }
        }
    }
}