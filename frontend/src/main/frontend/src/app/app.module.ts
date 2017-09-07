import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';

import {AppComponent} from './component/app/app.component';
import {AppUsersComponent} from './component/users/users.component';
import {AppUserDataService} from "./component/users/user.data.service";
import {routing} from './route/app.route';
import {PageNotFoundComponent} from "./component/pagenotfound/pagenotfound.component";

import {
  ChartModule, ConfirmDialogModule, FileUploadModule, PanelModule, GrowlModule, MenubarModule, DialogModule,
  ButtonModule, AutoCompleteModule, DataTableModule, SharedModule, DropdownModule, PickListModule, CheckboxModule,
  TriStateCheckboxModule, InputTextModule, InputTextareaModule, CalendarModule, PasswordModule, TabViewModule,
  ScheduleModule
} from 'primeng/primeng';

import {MaterialModule} from '@angular/material';
import {ConfirmationService} from "primeng/components/common/api";
import {BrowserAnimationsModule} from "@angular/platform-browser/animations";
import {LocationStrategy, HashLocationStrategy} from "@angular/common";
import {AuthService} from "./service/auth.service";
import {HomeComponent} from "./component/home/home.component";
import {ConfirmDeleteDialogComponent} from "./support/confirm-delete-dialog.component";
import {DoughnutChartComponent} from "./component/chart/doughnutchart/doughnutchart.component";
import {ScheduleChartComponent} from "./component/chart/schedulechart/schedulechart.component";
import {AppUsersAddComponent} from "./component/users/user.add.component";
import {RoleDataService} from "./component/role/role.data.service";
import {EmailValidator} from "./support/email.validator";
import {ReportDataService} from "./component/report/report.data.service";
import {RolesComponent} from "./component/role/role.component";
import {ReportComponent} from "./component/report/report.component";
import {ReportsAddComponent} from "./component/report/report.add.component";
import {FieldOfStudyDataService} from "./component/fieldofstudy/fieldofstudy.data.service";
import {FieldOfStudyComponent} from "./component/fieldofstudy/fieldofstudy.component";
import {SubjectDataService} from "./component/subject/subject.data.service";
import {FieldOfStudyAddComponent} from "./component/fieldofstudy/fieldofstudy.add.component";
import {SubjectComponent} from "./component/subject/subject.component";
import {LaboratoryDataService} from "./component/laboratory/laboratory.data.service";
import {SubjectAddComponent} from "./component/subject/subject.add.component";
import {LaboratoryComponent} from "./component/laboratory/laboratory.component";
import {LaboratoryAddComponent} from "./component/laboratory/laboratory.add.component";

@NgModule({
  declarations: [
    HomeComponent,
    AppComponent,
    AppUsersComponent,
    AppUsersAddComponent,
    RolesComponent,
    ReportComponent,
    ReportsAddComponent,
    FieldOfStudyComponent,
    FieldOfStudyAddComponent,
    SubjectComponent,
    SubjectAddComponent,
    LaboratoryComponent,
    LaboratoryAddComponent,
    PageNotFoundComponent,
    ConfirmDeleteDialogComponent,
    DoughnutChartComponent,
    ScheduleChartComponent,
    EmailValidator
  ],
  imports: [
    //angular
    BrowserModule,
    FormsModule,
    HttpModule,
    BrowserAnimationsModule,

    // angular material,
    MaterialModule,

    // primeng
    ConfirmDialogModule,
    FileUploadModule,
    PanelModule,
    GrowlModule,
    MenubarModule,
    DialogModule,
    ButtonModule,
    AutoCompleteModule,
    DataTableModule,
    SharedModule,
    DropdownModule,
    PickListModule,
    CheckboxModule,
    TriStateCheckboxModule,
    InputTextModule,
    InputTextareaModule,
    CalendarModule,
    PasswordModule,
    TabViewModule,
    ChartModule,
    ScheduleModule,

    // our application routes
    routing
  ],
  providers: [
    AppUserDataService,
    RoleDataService,
    ReportDataService,
    FieldOfStudyDataService,
    SubjectDataService,
    LaboratoryDataService,
    {provide: LocationStrategy, useClass: HashLocationStrategy}, //With this urls works directly from browser

    // our application services
    AuthService,

    // primeng service
    ConfirmationService
  ],
  entryComponents: [ConfirmDeleteDialogComponent],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }




