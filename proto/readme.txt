github 下载protoc-x.x.x-win32 解压获取.exe 复制到system32 目录下

配置maven 插件:

            <plugin>
                <groupId>org.xolstice.maven.plugins</groupId>
                <artifactId>protobuf-maven-plugin</artifactId>
                <version>0.5.1</version>
                <configuration>
                    <protoSourceRoot>${project.basedir}/proto</protoSourceRoot>
                    <outputDirectory>${os.detected.classifier}</outputDirectory>
                    <clearOutputDirectory>false</clearOutputDirectory>
                </configuration>
                <dependencies>
                    <dependency>
                        <groupId>com.google.protobuf</groupId>
                        <artifactId>protobuf-java</artifactId>
                        <version>${protobuf.version}</version>
                    </dependency>
                </dependencies>
                <executions>
                    <execution>
                        <goals>
                            <goal>compile</goal>
                            <goal>compile-custom</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>