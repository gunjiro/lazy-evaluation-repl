require "rake/clean"

JAR_FILE = "inter.jar"
CLASS_DIR = "class"
SRC_DIR = "src"
PARSER = "parser/Parser.jj"
PARSER_JAVA = "#{SRC_DIR}/Parser.java"
SRCS = FileList["#{SRC_DIR}/**/*.java"]
ENTRY_POINT = "Main"
ENTRY_CLASS = "#{CLASS_DIR}/#{ENTRY_POINT}.class"

directory CLASS_DIR
CLEAN.include CLASS_DIR
CLEAN.include PARSER_JAVA
CLOBBER.include JAR_FILE

task :default => JAR_FILE

task :run => JAR_FILE do |t|
    sh "java -jar #{t.prerequisites[0]}"
end

file JAR_FILE => [CLASS_DIR, PARSER_JAVA, ENTRY_CLASS] do |t|
    sh "jar -cfe #{t.name} #{ENTRY_POINT} -C #{CLASS_DIR} ."
end

file PARSER_JAVA => PARSER do |t|
    sh "javacc -OUTPUT_DIRECTORY:#{SRC_DIR} #{t.prerequisites[0]}"
end

file ENTRY_CLASS => SRCS do |t|
    sh "javac -Xlint:unchecked -cp #{CLASS_DIR} -d #{CLASS_DIR} -sourcepath #{SRC_DIR} #{t.prerequisites.join(' ')}"
end
