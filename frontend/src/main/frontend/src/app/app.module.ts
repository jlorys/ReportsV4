import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {HttpModule} from '@angular/http';

import {AppComponent} from './component/app/app.component';
import {AppUsersComponent} from './component/component.admin/users/users.component';
import {AppUserDataService} from "./component/component.admin/users/user.data.service";
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
import {AppUsersAddComponent} from "./component/component.admin/users/user.add.component";
import {RoleDataService} from "./component/component.admin/role/role.data.service";
import {EmailValidator} from "./support/email.validator";
import {ReportDataService} from "./component/component.admin/report/report.data.service";
import {RolesComponent} from "./component/component.admin/role/role.component";
import {ReportComponent} from "./component/component.admin/report/report.component";
import {ReportsAddComponent} from "./component/component.admin/report/report.add.component";
import {FieldOfStudyDataService} from "./component/component.admin/fieldofstudy/fieldofstudy.data.service";
import {FieldOfStudyComponent} from "./component/component.admin/fieldofstudy/fieldofstudy.component";
import {SubjectDataService} from "./component/component.admin/subject/subject.data.service";
import {FieldOfStudyAddComponent} from "./component/component.admin/fieldofstudy/fieldofstudy.add.component";
import {SubjectComponent} from "./component/component.admin/subject/subject.component";
import {LaboratoryDataService} from "./component/component.admin/laboratory/laboratory.data.service";
import {SubjectAddComponent} from "./component/component.admin/subject/subject.add.component";
import {LaboratoryComponent} from "./component/component.admin/laboratory/laboratory.component";
import {LaboratoryAddComponent} from "./component/component.admin/laboratory/laboratory.add.component";
import {UserReportComponent} from "./component/component.user/report/report.component";
import {UserReportsAddComponent} from "./component/component.user/report/report.add.component";
import {UserReportDataService} from "./component/component.user/report/report.data.service";
import {UserAccountDataService} from "./component/component.user/user.account/user.data.service";
import {UserAccountComponent} from "./component/component.user/user.account/user.account.component";
import {UserLaboratoryDetailComponent} from "./component/component.user/laboratory/laboratory.detail.component";
import {UserLaboratoryDataService} from "./component/component.user/laboratory/laboratory.data.service";
import {UserLaboratoryComponent} from "./component/component.user/laboratory/laboratory.component";
import {UserSubjectComponent} from "./component/component.user/subject/subject.component";
import {UserSubjectDetailComponent} from "./component/component.user/subject/subject.detail.component";
import {UserFieldOfStudyComponent} from "./component/component.user/fieldofstudy/fieldofstudy.component";
import {UserFieldOfStudyDetailComponent} from "./component/component.user/fieldofstudy/fieldofstudy.detail.component";
import {ReviewerReportComponent} from "./component/component.reviewer/report/report.component";
import {ReviewerReportsDetailComponent} from "./component/component.reviewer/report/report.detail.component";
import {ReviewerReportDataService} from "./component/component.reviewer/report/report.data.service";

@NgModule({
  declarations: [
    HomeComponent,
    AppComponent,

    //Admin components
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

    //User components
    UserReportComponent,
    UserReportsAddComponent,
    UserAccountComponent,
    UserLaboratoryComponent,
    UserLaboratoryDetailComponent,
    UserSubjectComponent,
    UserSubjectDetailComponent,
    UserFieldOfStudyComponent,
    UserFieldOfStudyDetailComponent,

    //Reviewer components
    ReviewerReportComponent,
    ReviewerReportsDetailComponent,

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
    //Admin role data services
    AppUserDataService,
    RoleDataService,
    ReportDataService,
    FieldOfStudyDataService,
    SubjectDataService,
    LaboratoryDataService,

    //User role data services
    UserReportDataService,
    UserAccountDataService,
    UserLaboratoryDataService,

    //Reviewer role data services
    ReviewerReportDataService,

    {provide: LocationStrategy, useClass: HashLocationStrategy}, //With this urls works directly from browser

    //our application services
    AuthService,

    //primeng service
    ConfirmationService
  ],
  entryComponents: [ConfirmDeleteDialogComponent],
  bootstrap: [
    AppComponent
  ]
})
export class AppModule { }




