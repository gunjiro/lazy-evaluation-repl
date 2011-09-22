require "rake/clean"

JUNIT_FILE = "junit.jar"
JAR_FILE = "inter.jar"
SHELL_FILE = JAR_FILE.ext
BAT_FILE = JAR_FILE.ext 'bat'
TEST_SHELL_FILE = JUNIT_FILE.ext
TEST_BAT_FILE = JUNIT_FILE.ext 'bat'
CLASS_DIR = "class"
MAIN_CLASS_DIR = "#{CLASS_DIR}/main"
TEST_CLASS_DIR = "#{CLASS_DIR}/test"
SRC_DIR = "src"
MAIN_SRC_DIR = "#{SRC_DIR}/main"
PARSER_SRC_DIR = "#{SRC_DIR}/parser"
TEST_SRC_DIR = "#{SRC_DIR}/test"
MAIN_SRCS = FileList["#{MAIN_SRC_DIR}/**/*.java"]
TEST_SRCS = FileList["#{TEST_SRC_DIR}/**/*.java"]
JJ_FILE = FileList["#{SRC_DIR}/Parser.jj"]
PARSER_FILE = "#{PARSER_SRC_DIR}/Parser.java"
ENTRY_POINT = "Main"
ENTRY_CLASS = "#{MAIN_CLASS_DIR}/#{ENTRY_POINT}.class"
TEST_MAIN_CLASS = "TestMain.class"
RAKE_FILE = "Rakefile"

directory MAIN_CLASS_DIR
directory PARSER_SRC_DIR
directory TEST_CLASS_DIR
CLEAN.include MAIN_CLASS_DIR
CLEAN.include TEST_CLASS_DIR
CLEAN.include PARSER_SRC_DIR
CLOBBER.include SHELL_FILE
CLOBBER.include BAT_FILE
CLOBBER.include JAR_FILE
CLOBBER.include TEST_SHELL_FILE
CLOBBER.include TEST_BAT_FILE

task :default => [SHELL_FILE, BAT_FILE]
task :main => [MAIN_CLASS_DIR, PARSER_SRC_DIR, PARSER_FILE, ENTRY_CLASS]
task :test => [:main, TEST_CLASS_DIR, TEST_SHELL_FILE, TEST_BAT_FILE]

file SHELL_FILE => [JAR_FILE, RAKE_FILE] do |t|
    f = open(t.name, "wb")
    f.puts "#!/bin/sh"
    f.puts "java -Xss8m -jar #{JAR_FILE} $*"
    f.close
    puts "create #{t.name}"
end

file BAT_FILE => [JAR_FILE, RAKE_FILE] do |t|
    f = open(t.name, "wb")
    f.puts "@echo off"
    f.puts "java -Xss8m -jar #{JAR_FILE} %*"
    f.close
    puts "create #{t.name}"
end

file JAR_FILE => :main do |t|
    sh "jar -cfe #{t.name} #{ENTRY_POINT} -C #{MAIN_CLASS_DIR} ."
end

file PARSER_FILE => JJ_FILE do |t|
    sh "javacc -OUTPUT_DIRECTORY:#{PARSER_SRC_DIR} #{t.prerequisites[0]}"
end

file ENTRY_CLASS => MAIN_SRCS do |t|
    sh "javac -Xlint:unchecked -cp #{MAIN_CLASS_DIR} -d #{MAIN_CLASS_DIR} -sourcepath #{MAIN_SRC_DIR};#{PARSER_SRC_DIR} #{t.prerequisites.join(' ')} #{PARSER_SRC_DIR}/*.java"
end

file TEST_SHELL_FILE => [TEST_MAIN_CLASS, RAKE_FILE] do |t|
    f = open(t.name, "wb")
    f.puts "#!/bin/sh"
    f.puts "java -cp \"#{MAIN_CLASS_DIR};#{TEST_CLASS_DIR};#{JUNIT_FILE}\" TestMain $*"
    f.close
    puts "create #{t.name}"
end

file TEST_BAT_FILE => [TEST_MAIN_CLASS, RAKE_FILE] do |t|
    f = open(t.name, "wb")
    f.puts "@echo off"
    f.puts "java -cp \"#{MAIN_CLASS_DIR};#{TEST_CLASS_DIR};#{JUNIT_FILE}\" TestMain %*"
    f.close
    puts "create #{t.name}"
end

file TEST_MAIN_CLASS => TEST_SRCS do |t|
    sh "javac -cp #{MAIN_CLASS_DIR};#{TEST_CLASS_DIR};#{JUNIT_FILE} -d #{TEST_CLASS_DIR} -sourcepath #{MAIN_SRC_DIR};#{PARSER_SRC_DIR};#{TEST_SRC_DIR} #{t.prerequisites.join(' ')}"
end
