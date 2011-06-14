count = 0     
File.open "someFile.txt" do |file|
   file.each { |line| count += 1 }
end 
p count