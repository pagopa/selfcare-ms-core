package it.pagopa.selfcare.mscore.core.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

@Configuration
@EnableScheduling
@PropertySource("classpath:config/core-config.properties")
public class SchedulerConfig implements SchedulingConfigurer {

    @Value("${scheduler.threads.max-number}")
    private int maxScheduleThreadNumber;

    @Value("${scheduler.fixed-delay.delay}")
    private Long fixedDelay;

    @Value("${scheduler.regenerate-kafka-queue-config.name}")
    private String regenerateKafkaQueueConfigName;

    @Value("${scheduler.regenerate-kafka-queue.enabled}")
    private boolean regnerateKafkaQueueEnabled;

    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
        taskScheduler.setPoolSize(maxScheduleThreadNumber);
        taskScheduler.setWaitForTasksToCompleteOnShutdown(true);
        taskScheduler.initialize();
        taskRegistrar.setTaskScheduler(taskScheduler);
    }

    private boolean sendOldEvent;
    public boolean getSendOldEvent() {
        return sendOldEvent;
    }

    public void setScheduler(boolean sendOldEvent) {
        this.sendOldEvent = sendOldEvent;
    }

    public Long getFixedDelay(){
        return fixedDelay;
    }

    public String getRegenerateKafkaQueueConfigName() {
        return regenerateKafkaQueueConfigName;
    }

    public boolean getRegnerateKafkaQueueEnabled() {
        return regnerateKafkaQueueEnabled;
    }

}
